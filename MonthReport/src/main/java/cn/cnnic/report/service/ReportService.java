package cn.cnnic.report.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import cn.cnnic.report.config.model.IndexConfigModel;
import cn.cnnic.report.config.model.ReportApplicationConfigModel;
import cn.cnnic.report.config.model.ReportChannelConfigModel;
import cn.cnnic.report.mapper.ReportDailyMapper;
import cn.cnnic.report.mapper.ReportTopMapper;
import cn.cnnic.report.po.ReportDailyPO;
import cn.cnnic.report.po.ReportTopPO;
import cn.cnnic.report.utils.CalendarUtil;
import cn.cnnic.report.utils.FileUtil;
import cn.cnnic.report.utils.PunycodeUtil;
import cn.cnnic.report.utils.StringUtil;
import cn.cnnic.report.vo.ReportChannelDailyDataVO;
import cn.cnnic.report.vo.ReportDailyVO;

@Service
public class ReportService {
	private static final String REPORT_DIRECTORY = "report";
	private static final String REPORT_FILENAME_SUFFIX = ".txt";
	private static final String KEYWORD_SUFFIX = ".keyword";
	private static final String ROOT_PATH = new File(ReportService.class.getResource("/").getPath()).getParentFile()
			.getParent() + File.separator;

	@Autowired
	private ReportApplicationConfigModel config;
	@Autowired
	private ReportDailyMapper dailyMapper;
	@Autowired
	private ReportTopMapper topMapper;
	@Autowired
	private ElasticsearchService esSerivce;

	private String indexPattern;// eg:logstash-ccwhoisd-2019.05.*
	private String filePath;// Absolute file path of report file
	private String indexKeyword;// For constructing indexPattern or saving database
	private String reportDate;// For constructing indexPattern or saving database
	private String conditionField;// Used with includes or excludes to distinguish channels
	private List<ReportChannelConfigModel> reportChannelList;// 用来生成服务各个类型报告的总数

	/*
	 * 
	 */
	public synchronized ServiceResponse generateReport(String indexKeyword, String reportDate) throws IOException {
		this.indexKeyword = indexKeyword;
		this.reportDate = reportDate;
		// step1: return if the file exists,otherwise create it(including its parent
		// directories if not exist)
		this.filePath = ROOT_PATH + REPORT_DIRECTORY + File.separator + indexKeyword + File.separator + reportDate
				+ REPORT_FILENAME_SUFFIX;
		if (FileUtil.isExists(filePath)) {
			return new ServiceResponse(ServiceResponse.EXISTS,indexKeyword + "(" + reportDate + ")");
		} else {
			FileUtil.createFile(filePath);
		}
		//step2: delete from the database
		dailyMapper.delete(indexKeyword, reportDate + "-01",
				CalendarUtil.getNextMonthFirstDay(reportDate + "-01", "yyyy-MM-dd"));
		topMapper.delete(indexKeyword, reportDate);
		this.indexPattern = "logstash-" + indexKeyword + "-"
				+ CalendarUtil.convertFormat(reportDate, "yyyy-MM", "yyyy.MM") + ".*";

		List<IndexConfigModel> indexConfig = config.getIndexes();
		for (IndexConfigModel index : indexConfig) {
			String serviceNameConfig = index.getName();
			if (indexKeyword.equals(serviceNameConfig)) {
				this.conditionField = StringUtil.excludeSuffix(index.getConditionField(), KEYWORD_SUFFIX);
				this.reportChannelList = index.getChannels();
				for (ReportChannelConfigModel reportChannel : this.reportChannelList) {
					String reportChannelName = reportChannel.getName();
					String[] fields = reportChannel.getFields();
					String[] includes = reportChannel.getIncludes();
					String[] excludes = reportChannel.getExcludes();
					// step3: generate top100 for all fields
					fetchTopDataAndWriteFile(reportChannelName, fields, includes, excludes);
					// step4：generate daily counts
					fetchDailyDataAndWriteFile(reportChannelName, includes, excludes);
				}
				break;
			}
		}
		// step5:generate total counts for all channels
		fetchTotalDataAndWriteFile();
		return new ServiceResponse(ServiceResponse.OK,indexKeyword + "(" + reportDate + ")");
	}

	/*
	 * generate query builder,do search,write into file
	 */
	private void fetchTotalDataAndWriteFile() throws IOException {
		FileUtil.init(filePath);
		FileUtil.writeFile("##########total#############" + "\n");
		for (ReportChannelConfigModel reportType : reportChannelList) {
			String reportTypeName = reportType.getName();
			String[] includes = reportType.getIncludes();
			String[] excludes = reportType.getExcludes();
			SearchResponse searchResponse = esSerivce.doSearch(this.indexPattern,
					generateQueryBuilder(includes, excludes, conditionField), null);
			FileUtil.writeFile(reportTypeName + "	" + searchResponse.getHits().totalHits + "\n");
		}
		FileUtil.commit();
	}

	/*
	 * generate query builder and aggregation for es search. do
	 * search. write into file and database.
	 */
	private void fetchDailyDataAndWriteFile(String reportTypeName, String[] includes, String[] excludes)
			throws IOException {
		List<ReportDailyPO> dailyToBeInsertedList = new ArrayList<ReportDailyPO>();
		DateHistogramAggregationBuilder aggregation = AggregationBuilders.dateHistogram("group_by_daily")
				.dateHistogramInterval(DateHistogramInterval.DAY).field("@timestamp").format("yyyy-MM-dd");
		SearchResponse searchResponse = esSerivce.doSearch(this.indexPattern,
				generateQueryBuilder(includes, excludes, conditionField), aggregation);
		FileUtil.init(filePath);
		FileUtil.writeFile("##############" + reportTypeName + " by day##############" + "\n");
		if (searchResponse.getHits().totalHits != 0) {
			ParsedDateHistogram terms = searchResponse.getAggregations().get("group_by_daily");
			for (org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Bucket bucket : terms.getBuckets()) {
				String reportDayStr = bucket.getKeyAsString();
				if (!this.reportDate.equals(CalendarUtil.convertFormat(reportDayStr, "yyyy-MM-dd", "yyyy-MM"))) {
					continue;
				}
				long docCount = bucket.getDocCount();
				FileUtil.writeFile(reportTypeName + "	" + reportDayStr + "	" + docCount + "\n");
				ReportDailyPO reportDailyPO = new ReportDailyPO.Builder().serviceName(indexKeyword)
						.reportType(reportTypeName).reportDate(reportDayStr).docCount(docCount).build();
				dailyToBeInsertedList.add(reportDailyPO);
			}
		}
		FileUtil.commit();

		if (dailyToBeInsertedList.size() > 0) {
			dailyMapper.batchInsert(dailyToBeInsertedList);
		}
	}

	/*
	 * generate query builder and aggregation for es search. do search. write into
	 * file and database.
	 */
	private void fetchTopDataAndWriteFile(String reportTypeName, String[] fields, String[] includes, String[] excludes)
			throws IOException {
		List<ReportTopPO> topToBeInsertedList = new ArrayList<ReportTopPO>();
		if (null != fields) {
			for (String fieldName : fields) {
				String groupBy = "group_by_" + StringUtil.excludeSuffix(fieldName, KEYWORD_SUFFIX);
				TermsAggregationBuilder termsAggregation = AggregationBuilders.terms(groupBy)
						.field(StringUtil.includeSuffix(fieldName, KEYWORD_SUFFIX)).size(100);
				SearchResponse searchResponse = esSerivce.doSearch(this.indexPattern,
						generateQueryBuilder(includes, excludes, conditionField), termsAggregation);
				FileUtil.init(filePath);
				FileUtil.writeFile("##############" + reportTypeName + " top "
						+ StringUtil.excludeSuffix(fieldName, KEYWORD_SUFFIX) + " 100##############" + "\n");
				if (searchResponse.getHits().totalHits != 0) {
					Terms terms = searchResponse.getAggregations().get(groupBy);
					for (Bucket bucket : terms.getBuckets()) {
						String fieldValue = bucket.getKeyAsString();
						long docCount = bucket.getDocCount();
						FileUtil.writeFile(reportTypeName + "	" + PunycodeUtil.evaluate(fieldValue) + "	" + docCount + "\n");
						ReportTopPO reportTopPO = new ReportTopPO.Builder().serviceName(indexKeyword)
								.reportType(reportTypeName).reportDate(reportDate)
								.fieldName(StringUtil.excludeSuffix(fieldName, KEYWORD_SUFFIX)).fieldValue(fieldValue)
								.docCount(docCount).build();
						topToBeInsertedList.add(reportTopPO);
					}
				}
				FileUtil.commit();
			}
		}
		if (topToBeInsertedList.size() > 0) {
			topMapper.batchInsert(topToBeInsertedList);
		}

	}

	private BoolQueryBuilder generateQueryBuilder(String[] includes, String[] excludes, String conditionField) {
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		if (null != includes) {
			for (String includeIp : includes) {
				if (includeIp.endsWith("*") || includeIp.startsWith("*")) {
					queryBuilder = queryBuilder.should(QueryBuilders.wildcardQuery(conditionField, includeIp));
				} else {
					queryBuilder = queryBuilder.should(QueryBuilders.termQuery(conditionField, includeIp));
				}

			}
		}
		if (null != excludes) {
			for (String excludeIp : excludes) {
				if (excludeIp.endsWith("*") || excludeIp.startsWith("*")) {
					queryBuilder = queryBuilder.mustNot(QueryBuilders.wildcardQuery(conditionField, excludeIp));
				} else {
					queryBuilder = queryBuilder.mustNot(QueryBuilders.termQuery(conditionField, excludeIp));
				}
			}
		}
		return queryBuilder;
	}

	public ResponseEntity<byte[]> fetchReportFile(String serviceName, String reportDate) throws IOException {
		String ROOT_PATH = new File(ReportService.class.getResource("/").getPath()).getParentFile().getParent()
				+ File.separator;
		String filePath = ROOT_PATH + REPORT_DIRECTORY + File.separator + serviceName + File.separator + reportDate
				+ REPORT_FILENAME_SUFFIX;
		String fileName = serviceName + "-" + reportDate + REPORT_FILENAME_SUFFIX;
		String downloadFileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");// 设置编码

		File reportFile = new File(filePath);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDispositionFormData("attachment", downloadFileName);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(reportFile), headers, HttpStatus.CREATED);
	}

	public ReportDailyVO dailyCount(String indexName, String reportDate) {
		this.indexKeyword = indexName;
		this.reportDate = reportDate;
		ReportDailyVO result = new ReportDailyVO();
		String[] days = buildDays(this.reportDate);
		List<ReportChannelDailyDataVO> channelDayCounts = buildChannelDayCounts(days);

		result.setDescription(this.indexKeyword + " "
				+ CalendarUtil.convertFormat(this.reportDate, "yyyy-MM", "yyyy年MM月") + " 每日查询量");
		result.setChannelDayCounts(channelDayCounts);
		result.setDays(days);
		return result;
	}

	private List<ReportChannelDailyDataVO> buildChannelDayCounts(String[] days) {
		List<ReportChannelDailyDataVO> channelDayCounts = new ArrayList<ReportChannelDailyDataVO>();

		// read channels from config file
		List<IndexConfigModel> indexConfig = config.getIndexes();
		for (IndexConfigModel index : indexConfig) {
			String serviceNameConfig = index.getName();
			if (indexKeyword.equals(serviceNameConfig)) {
				this.reportChannelList = index.getChannels();
				break;
			}
		}

		for (ReportChannelConfigModel reportChannelVO : reportChannelList) {
			String reportChannelName = reportChannelVO.getName();
			List<ReportDailyPO> reportDailyPOList = dailyMapper.query(this.indexKeyword, reportChannelName,
					this.reportDate + "-01", CalendarUtil.getNextMonthFirstDay(this.reportDate + "-01", "yyyy-MM-dd"),
					"REPORT_DATE");
			ReportChannelDailyDataVO dataVO = new ReportChannelDailyDataVO();
			long[] counts = new long[days.length];
			for (int i = 0; i < reportDailyPOList.size(); i++) {
				ReportDailyPO reportDailyPO = reportDailyPOList.get(i);
				String day = reportDailyPO.getReportDate();
				int idx = Integer.valueOf(day.split("-")[2]) - 1;
				counts[idx] = reportDailyPO.getDocCount();
			}

			dataVO.setName(reportChannelName);
			dataVO.setData(counts);
			channelDayCounts.add(dataVO);
		}
		return channelDayCounts;
	}

	private String[] buildDays(String yearMonth) {
		String startDate = yearMonth + "-01";
		String endDate = CalendarUtil.getNextMonthFirstDay(startDate, "yyyy-MM-dd");
		String[] result = new String[CalendarUtil.getDiffDays(startDate, endDate)];
		String tempDate = startDate;
		for (int i = 0; i < result.length; i++) {
			result[i] = tempDate;
			if (CalendarUtil.getDiffDays(tempDate, endDate) > 1) {
				tempDate = CalendarUtil.getIntervalDate(tempDate, 1);
			} else {
				break;
			}
		}
		return result;
	}

	public boolean deleteFile(String indexName, String reportDate) {
		String filePath = ROOT_PATH + REPORT_DIRECTORY + File.separator + indexKeyword + File.separator + reportDate
				+ REPORT_FILENAME_SUFFIX;
		File reportFile = new File(filePath);
		if (FileUtil.isExists(reportFile)) {
			reportFile.delete();
			return true;
		} else {
			return false;
		}
	}
}

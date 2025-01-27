package cn.cnnic.domainstat.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.cnnic.domainstat.config.ApplicationConfigModel;
import cn.cnnic.domainstat.config.CnDateRangeModel;
import cn.cnnic.domainstat.mapper.CDomainMapper;
import cn.cnnic.domainstat.mapper.EppContactMapper;
import cn.cnnic.domainstat.mapper.EppEntAllDomainMapper;
import cn.cnnic.domainstat.po.CDomainPO;
import cn.cnnic.domainstat.po.EppContactPO;
import cn.cnnic.domainstat.po.EppEntAllDomainPO;
import cn.cnnic.domainstat.utils.CalendarUtil;
import cn.cnnic.domainstat.utils.EmailUtil;
import cn.cnnic.domainstat.utils.FileUtil;
import cn.cnnic.domainstat.utils.SpMap;
import cn.cnnic.domainstat.utils.SpReMap;
import cn.cnnic.domainstat.utils.StringUtil;
import cn.cnnic.domainstat.utils.UnitMap;
import net.cnnic.borlan.utils.lookup.PostalCodeUtil;

@Service
public class DomainStatService {
	@Autowired
	private EppContactMapper contactMapper;
	@Autowired
	private EppEntAllDomainMapper eppEntAllDomainMapper;
	@Autowired
	private SpMap spMap;
	@Autowired
	private SpReMap spReMap;
	@Autowired
	private UnitMap unitMap;
	@Autowired
	private PostalCodeUtil pcu;
	@Autowired
	private ApplicationConfigModel config;
	@Autowired
	SqlSessionTemplate template;

	private String RESULT_DIRECTORY;
	private int THRESHOLD;

	private String APPLICATION_HOME_PATH = System.getProperty("user.dir") + "/";
	private static final String LOGGER_DECLARING_TYPE = DomainStatService.class.getName();

	private final static String SRC_FILE_SUFFIX = "-src.txt";
	private final static String RESULT_FILE_SUFFIX = ".txt";
	private final static int MAX_THRESHOLD = 10000000;
	private final static int MIN_THRESHOLD = 1500000;
	private final static int DEFAULT_THRESHOLD = 4000000;

	private final static int DIFF_VALUE = 1000;
	private final static int RETRY_TIMES = 3;

	private final static Logger LOGGER = LoggerFactory.getLogger(DomainStatService.class);
	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;

	public void fetchData() throws Exception {
		init();
		if (isRangesConfigured()) {
			customFetch();
			return;
		} 
		FileUtil.deleteFileOfSuffix(RESULT_DIRECTORY, RESULT_FILE_SUFFIX);
		fetchCdnCn();
		fetchCdnZhongguo();
		fetchCn();
		compressAndUpload();
	}

	private void customFetch() throws Exception {
		List<CnDateRangeModel> ranges = config.getRanges();
		if (null != ranges && ranges.size() != 0) {
			for (CnDateRangeModel range : ranges) {
				if (StringUtils.isBlank(range.getStart()) && StringUtils.isBlank(range.getEnd())) {
					break;
				}
				String startDate = StringUtils.isBlank(range.getStart()) ? CalendarUtil.ORIGINAL_DATE
						: (CalendarUtil.getThisMonthFirstDay(range.getStart()));
				String endDate = StringUtils.isBlank(range.getEnd()) ? CalendarUtil.getThisMonthLastDay(new Date())
						: CalendarUtil.getThisMonthLastDay(
								CalendarUtil.convertFormat(range.getEnd(), CalendarUtil.DEFAULT_FORMAT));
				String filePrefix = range.getName();
				if (StringUtils.isBlank(filePrefix)) {
					filePrefix = calFilePrefix(startDate, endDate);
				}
				checkAndWriteCn(startDate, endDate, filePrefix);
			}
		}
	}

	private boolean isRangesConfigured() {
		List<CnDateRangeModel> ranges = config.getRanges();
		return null != ranges && ranges.size() != 0;
	}

	/*
	 * set the result directory and the threshold.
	 */
	private void init() throws IOException {
		if (StringUtils.isNotBlank(config.getResult())) {
			RESULT_DIRECTORY = StringUtil.includeSuffix(config.getResult(), "/");
		} else {
			RESULT_DIRECTORY = APPLICATION_HOME_PATH + "result/";
		}
		LOGGER.info("RESULT DIRECTORY IS SET TO => " + RESULT_DIRECTORY);

		String threshold = config.getThreshold();
		if (StringUtils.isBlank(threshold)) {
			THRESHOLD = DEFAULT_THRESHOLD;
		} else if (StringUtil.isNumber(threshold)) {
			THRESHOLD = Integer.valueOf(threshold);
		} else if (unitMap.isUnit(threshold)) {
			THRESHOLD = Integer.valueOf(threshold.substring(0, threshold.length() - 1))
					* unitMap.get(String.valueOf(threshold.charAt(threshold.length() - 1)));
		} else {
			THRESHOLD = DEFAULT_THRESHOLD;
		}
		if (THRESHOLD < MIN_THRESHOLD) {
			THRESHOLD = MIN_THRESHOLD;
		}
		if (THRESHOLD > MAX_THRESHOLD) {
			THRESHOLD = MAX_THRESHOLD;
		}

		LOGGER.info("THRESHOLD IS SET TO => " + THRESHOLD);
	}

	private void compressAndUpload() throws IOException {
		Runtime run = Runtime.getRuntime();
		LOGGER.info("EXECUTING SCRIPT FILE => " + APPLICATION_HOME_PATH + "bin/upload.sh " + RESULT_DIRECTORY + " >> "
				+ APPLICATION_HOME_PATH + "logs/domainstat.log");
		run.exec(APPLICATION_HOME_PATH + "bin/upload.sh " + RESULT_DIRECTORY + " >> " + APPLICATION_HOME_PATH
				+ "logs/domainstat.log");
	}

	private void fetchCdnCn() throws Exception {
		List<CnDateRangeModel> ranges = config.getRanges();
		if (null == ranges || ranges.size() == 0) {
			writeCdn("cdn-cn", "%.cn");
		}

	}

	private void fetchCdnZhongguo() throws Exception {
		List<CnDateRangeModel> ranges = config.getRanges();
		if (null == ranges || ranges.size() == 0) {
			writeCdn("cdn-zhongguo", "%.中国");
		}
	}

	private void fetchCn() throws Exception {
		checkAndWriteCn(CalendarUtil.ORIGINAL_DATE, "2009-12-31", "before2010");
		int currentYear = CalendarUtil.getCurrentYear();
		for (int year = 2010; year < currentYear; year++) {
			checkAndWriteCn(year + "-01-01", year + "-12-31", year + "");
		}
		checkAndWriteCn(currentYear + "-01-01", CalendarUtil.getThisMonthLastDay(new Date()), currentYear + "");
	}

	/*
	 * fetch cn data ,check if the data count is beyond the threshold and split it
	 * by year,half year,quater or month,util the count is below the threshold,then
	 * write files
	 */
	private void checkAndWriteCn(String startDate, String endDate, String filePrefix) throws Exception {
		if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
			LOGGER.error("ERR PARAMS => [" + LOGGER_DECLARING_TYPE + ".writeCn(" + startDate + "," + endDate + ","
					+ filePrefix + ") startDate and endDate cannot be blank!]");
			return;
		}
		startDate = CalendarUtil.convertFormat(startDate, CalendarUtil.DEFAULT_FORMAT);
		endDate = CalendarUtil.convertFormat(endDate, CalendarUtil.DEFAULT_FORMAT);
		int count = eppEntAllDomainMapper.queryCount(startDate, endDate);
		if (count == 0) {
			return;
		}
		if (count > THRESHOLD) {
			split(startDate, endDate, filePrefix);
			return;

		}
		// step1: fetch the cn domain list from the database,write the cn domain into
		// the source file
		writeCnDomainIntoSrcFile(startDate, endDate, filePrefix);
		int retry = RETRY_TIMES;
		while (isNotCompleteCnData(count, filePrefix) && retry > 0) {
			writeCnDomainIntoSrcFile(startDate, endDate, filePrefix);
			retry--;
		}
		if (isNotCompleteCnData(count, filePrefix) && retry == 0) {
			EmailUtil.sendEmail("zhangtao@cnnic.cn", "CN data "+filePrefix+" still got too big intervals after retrying "+RETRY_TIMES+" times!", "");
			return;
		}else {
			doWriteCn(startDate, endDate, filePrefix);	
		}
	}

	private boolean isNotCompleteCnData(int count, String filePrefix) throws IOException {
		return (Math.abs(count - FileUtil.calFileLines(RESULT_DIRECTORY + filePrefix + SRC_FILE_SUFFIX)) > DIFF_VALUE);
	}

	private void split(String startDate, String endDate, String filePrefix) throws Exception {
		if (!CalendarUtil.isSameYear(startDate, endDate)) {
			int startYear = CalendarUtil.getYear(startDate);
			int endYear = CalendarUtil.getYear(endDate);
			if (endYear < startYear) {
				LOGGER.error("ERR PARAMS => [" + LOGGER_DECLARING_TYPE + ".writeCn(" + startDate + "," + endDate + ","
						+ filePrefix + ") the year of endDate cannot be less than the year of startDate!]");
				return;
			}
			String thisYearLastDay = CalendarUtil.getThisYearLastDay(startDate);
			checkAndWriteCn(startDate, thisYearLastDay, calFilePrefix(startDate, thisYearLastDay));
			startDate = CalendarUtil.getNextYearFirstDay(startDate);
			for (int i = startYear + 1; i < endYear; i++) {
				thisYearLastDay = CalendarUtil.getThisYearLastDay(startDate);
				checkAndWriteCn(startDate, thisYearLastDay, CalendarUtil.convertFormat(startDate, "yyyy"));
				startDate = CalendarUtil.getNextYearFirstDay(startDate);
			}
			checkAndWriteCn(startDate, endDate, calFilePrefix(startDate, endDate));
		} else {
			int startMonth = CalendarUtil.getMonth(startDate);
			int endMonth = CalendarUtil.getMonth(endDate);
			if (endMonth < startMonth) {
				LOGGER.error("ERR PARAMS => [" + LOGGER_DECLARING_TYPE + ".writeCn(" + startDate + "," + endDate + ","
						+ filePrefix + ") the month of endDate cannot be less than the year of startDate!]");
				return;
			}
			if (!CalendarUtil.isSameHalfYear(startDate, endDate)) {
				String lastDayOfFirstHalfYear = CalendarUtil.getTheLastDayOfTheFirstHalfYear(startDate);
				String firstDayOfSecondHalfYear = CalendarUtil.getTheFirstDayOfTheSecondHalfYear(startDate);
				checkAndWriteCn(startDate, lastDayOfFirstHalfYear, calFilePrefix(startDate, lastDayOfFirstHalfYear));
				checkAndWriteCn(firstDayOfSecondHalfYear, endDate, calFilePrefix(firstDayOfSecondHalfYear, endDate));
			} else if (!CalendarUtil.isSameQurter(startDate, endDate)) {
				String lastDayOfThisQuarter = CalendarUtil.getTheLastDayOfThisQuarter(startDate);
				String firstDayOfTheNextQuarter = CalendarUtil.getTheFirstDayOfThisQuarter(endDate);
				checkAndWriteCn(startDate, lastDayOfThisQuarter, calFilePrefix(startDate, lastDayOfThisQuarter));
				checkAndWriteCn(firstDayOfTheNextQuarter, endDate, calFilePrefix(firstDayOfTheNextQuarter, endDate));

			} else if (!CalendarUtil.isSameMonth(startDate, endDate)) {
				for (int i = startMonth; i <= endMonth; i++) {
					checkAndWriteCn(startDate, CalendarUtil.getThisMonthLastDay(startDate),
							CalendarUtil.convertFormat(startDate, "yyyyMM"));
					startDate = CalendarUtil.getNextMonthFirstDay(startDate, CalendarUtil.DEFAULT_FORMAT);
				}
			} else {
				LOGGER.error("ERR PARAMS => [" + LOGGER_DECLARING_TYPE + ".writeCn(" + startDate + "," + endDate + ","
						+ filePrefix
						+ ") still beyond the threshold,but the minimum unit is month,try to decrease the threshold!]");
				return;
			}
		}
	}

	/*
	 * step1: fetch the contact list from the database(only fetch specific data from the
	 * startDate to endDate,not all contacts). step2: convert the contact list into
	 * a map between the contact id and the province,set the contact list to null
	 * for gc. step3: read the source file,get the province from the map by the key
	 * contact id(the last word of the source file) and write into the result file
	 * 
	 * @param startDate It is null before 2010,not null after 2010
	 * 
	 * @param endDate It may be null in the current year,not null before the current
	 * year
	 * 
	 * @param filePrefix
	 */
	private void doWriteCn(String startDate, String endDate, String filePrefix) throws Exception {
		LOGGER.info("#################################BEGIN TO FETCH CN " + startDate + " TO " + endDate
				+ " DATA#################################");
		LOGGER.info("PRINT METHOD PARAMS => [" + LOGGER_DECLARING_TYPE + ".doWriteCn(" + startDate + "," + endDate + ","
				+ filePrefix + ")]");
		long startTime = System.currentTimeMillis();
		// step1: fetch the contact list from the database(only fetch specific data from
		// the startDate to endDate,not all contacts)
		int count= contactMapper.queryCountWithCn(startDate, endDate);
		List<EppContactPO> contactList = contactMapper.queryWithCn(startDate, endDate);
		int retry=RETRY_TIMES;
		while(Math.abs(count-contactList.size())>DIFF_VALUE&&retry>0) {
			contactList = contactMapper.queryWithCn(startDate, endDate);
			retry--;
		}
		if(Math.abs(count-contactList.size())>DIFF_VALUE&&retry==0) {
			EmailUtil.sendEmail("zhangtao@cnnic.cn", "CN contact data "+filePrefix+" still got too big intervals after retrying "+RETRY_TIMES+" times!", "");
			return;
		}
		
		// step2: convert the contact list into a map between the contact id and the
		// province,set the contact list to null for gc.
		Map<String, String> contactToProvinceMap = buildMapOfContactIdToProvince(contactList);
		contactList = null;
		LOGGER.info("PRINT OBJECT SIZE => [" + LOGGER_DECLARING_TYPE + ".doWriteCn(" + startDate + "," + endDate + ","
				+ filePrefix + ") the size of contactToProvinceMap is " + contactToProvinceMap.size() + "]");
		// step3: read the source file,get the province from the map by the key contact
		// id(the last word of the source file) and write into the result file
		writeIntoResultFileBySrcFileAndMap(filePrefix, contactToProvinceMap);

		long endTime = System.currentTimeMillis();
		LOGGER.info("PRINT EXECUTION TIME => [" + LOGGER_DECLARING_TYPE + ".doWriteCn(" + startDate + "," + endDate
				+ "," + filePrefix + ") took " + (endTime - startTime) + "ms]");

		LOGGER.info("#################################FINISHED TO FETCH CN " + startDate + " TO " + endDate
				+ " DATA#################################");
	}

	private static String calFilePrefix(String startDate, String endDate) {
		if (CalendarUtil.isSameYear(startDate, endDate) && CalendarUtil.getDiffMonths(startDate, endDate) == 11) {
			return CalendarUtil.getYear(startDate) + "";
		} else if (CalendarUtil.isSameMonth(startDate, endDate)) {
			return CalendarUtil.convertFormat(startDate, "yyyyMM");
		} else {
			return CalendarUtil.convertFormat(startDate, "yyyyMM") + "-"
					+ CalendarUtil.convertFormat(endDate, "yyyyMM");
		}
	}

	/*
	 * write the cn domain list into the source file, set the cn domain list to null
	 * for gc.
	 */
	private void writeCnDomainIntoSrcFile(String startDate, String endDate, String filePrefix) throws IOException {
		LOGGER.info("PRINT METHOD PARAMS => [" + LOGGER_DECLARING_TYPE + ".writeCnDomainIntoSrcFile(" + startDate + ","
				+ endDate + "," + filePrefix + ")]");
		long startTime = System.currentTimeMillis();
		FileUtil.init(RESULT_DIRECTORY + filePrefix + SRC_FILE_SUFFIX);
		Map<String, String> params = new HashMap<String, String>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		template.select("cn.cnnic.domainstat.mapper.EppEntAllDomainMapper.query", params,
				new ResultHandler<EppEntAllDomainPO>() {

					@Override
					public void handleResult(ResultContext<? extends EppEntAllDomainPO> resultContext) {
						EppEntAllDomainPO domainPO = resultContext.getResultObject();
						String domainName = domainPO.getDomainName();
						String registrarId = domainPO.getSponsorRegrid();
						String registrantId = domainPO.getRegistrantId();
						try {
							FileUtil.writeFile(domainName + "," + registrarId + "," + registrantId + "\n");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				});
		FileUtil.commit();

		long endTime = System.currentTimeMillis();
		LOGGER.info("PRINT EXECUTION TIME => [" + LOGGER_DECLARING_TYPE + ".writeCnDomainIntoSrcFile(" + startDate + ","
				+ endDate + "," + filePrefix + ") took " + (endTime - startTime) + "ms]");
	}

	/*
	 * step1: fetch the cdn domain list from the database and write the cdn
	 * domain list into the source file.
	 * step2: fetch the contact list from the database(only fetch specific data by a
	 * pattern of like,not all contacts) step3: convert the contact list into a map
	 * between the contact id and the province,set the contact list to null for gc.
	 * step4: read the source file,get the province from the map by the key contact
	 * id(the last word of the source file) and write into the result file
	 */
	private void writeCdn(String filePrefix, String likePattern) throws Exception {
		LOGGER.info("#################################BEGIN TO FETCH " + filePrefix
				+ " DATA#################################");

		LOGGER.info("PRINT METHOD PARAMS => [" + LOGGER_DECLARING_TYPE + ".writeCdn(" + filePrefix + "," + likePattern
				+ ")]");
		long startTime = System.currentTimeMillis();
		// step1: fetch the cdn domain from the database,write the cdn domain into the source file
		writeCdnDomainIntoSrcFile(filePrefix, likePattern);

		// step2: fetch the contact list from the database(only fetch specific data by a
		// pattern of like,not all contacts)
		List<EppContactPO> contactList = contactMapper.queryWithCdn(CalendarUtil.format(new Date(), "yyyy-MM-dd"),
				likePattern);
		// step3: convert the contact list into a map between the contact id and the
		// province,set the contact list to null for gc.
		Map<String, String> contactToProvinceMap = buildMapOfContactIdToProvince(contactList);
		contactList = null;
		// step4: read the source file,get the province from the map by the key contact
		// id(the last word of the source file) and write into the result file
		writeIntoResultFileBySrcFileAndMap(filePrefix, contactToProvinceMap);

		long endTime = System.currentTimeMillis();
		LOGGER.info("PRINT EXECUTION TIME => [" + LOGGER_DECLARING_TYPE + ".writeCdn(" + filePrefix + "," + likePattern
				+ ") took " + (endTime - startTime) + "ms]");
		LOGGER.info("#################################FINISHED TO FETCH " + filePrefix
				+ " DATA#################################");
	}

	/*
	 * fetch the cdn domain from the database,write the cdn domain into the source file
	 */
	private void writeCdnDomainIntoSrcFile(String filePrefix, String likePattern) throws IOException {
		FileUtil.init(RESULT_DIRECTORY + filePrefix + SRC_FILE_SUFFIX);
		Map<String, String> params = new HashMap<String, String>();
		params.put("startDate", CalendarUtil.ORIGINAL_DATE);
		params.put("endDate", CalendarUtil.format(new Date(), "yyyy-MM-dd"));
		params.put("domainName", likePattern);
		template.select("cn.cnnic.domainstat.mapper.CDomainMapper.query", params, new ResultHandler<CDomainPO>() {

			@Override
			public void handleResult(ResultContext<? extends CDomainPO> resultContext) {
				CDomainPO domainPO = resultContext.getResultObject();
				String domainName = domainPO.getDomainName();
				String registrarId = domainPO.getRegistrarId();
				String registrantId = domainPO.getRegistrantId();
				try {
					FileUtil.writeFile(domainName + "," + registrarId + "," + registrantId + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		});
		FileUtil.commit();
	}

	/*
	 * read the source file,get the province from the map by the key contact id(the
	 * last word of the source file) and write into the result file
	 */
	private void writeIntoResultFileBySrcFileAndMap(String resultFilePrefix, Map<String, String> contactToProvinceMap)
			throws IOException {
		FileUtil.init(RESULT_DIRECTORY + resultFilePrefix + SRC_FILE_SUFFIX, FileUtil.READ);
		FileUtil.init(RESULT_DIRECTORY + resultFilePrefix + RESULT_FILE_SUFFIX, FileUtil.WRITE);
		String line = null;
		while ((line = FileUtil.readLine()) != null) {
			String[] arr = line.split(",");
			if (arr.length == 3) {
				String domainName = arr[0];
				String registrarId = arr[1];
				String registrantId = arr[2];
				String province = contactToProvinceMap.get(registrantId);
				FileUtil.writeFile(domainName + "\t" + registrarId + "\t" + province + "\n");
			}
		}
		FileUtil.commit();
		FileUtil.deleteFile(RESULT_DIRECTORY + resultFilePrefix + SRC_FILE_SUFFIX);

	}

	/*
	 * convert the contact list into a map between the contact id and the
	 * province,set the contact list to null for gc.
	 */
	private Map<String, String> buildMapOfContactIdToProvince(List<EppContactPO> contactList) {
		Map<String, String> contactToProvinceMap = new HashMap<String, String>();
		int size = contactList.size();
		for (int i = 0; i < size; i++) {
			EppContactPO contact = contactList.get(i);
			String contactId = contact.getId();
			String province = buildProvince(contact);
			contactList.set(i, null);
			contactToProvinceMap.put(contactId, province);
		}
		return contactToProvinceMap;
	}

	private String buildProvince(EppContactPO contact) {
		if (null == contact) {
			return "unkown";
		}
		String registrantPC = contact.getAddrPc();
		String registrantSP = contact.getAddrSp();
		String registrantStreet = contact.getAddrStreet();
		if (registrantPC == null || registrantPC.trim().equals("")) {
			registrantPC = "999999";
		}
		if (registrantSP == null || registrantSP.trim().equals("")) {
			registrantSP = "NO_STATEPROVINCE";
		}
		String province = pcu.getProvince(registrantPC).toLowerCase();
		if (province.equals("unknown")) {
			for (String key : spMap.keySet()) {
				Pattern pattern = Pattern.compile("^(中国)?[\\s\\.·?，．]?" + key + "省?",
						Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
				Matcher matcher = pattern.matcher(registrantStreet);
				if (matcher.matches()) {
					province = spMap.get(key);
					break;
				}
			}
		}
		// according to address
		if (province.equals("unknown")) {
			for (String key : spMap.keySet()) {
				Pattern pattern = Pattern.compile(key, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
				Matcher matcher = pattern.matcher(registrantStreet);
				if (matcher.matches()) {
					province = spMap.get(key);
					break;
				}
			}
		}

		// according to province fields
		if (province.equals("unknown") && !registrantSP.equals("NO_STATEPROVINCE")) {
			for (String key : spReMap.keySet()) {
				Pattern pattern = Pattern.compile(key, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
				Matcher matcher = pattern.matcher(registrantSP);
				if (matcher.matches()) {
					province = spReMap.get(key);
					break;
				}
			}
		}
		if ("tw".equals(province) || "mo".equals(province) || "hk".equals(province) || "foreign".equals(province)) {
			province = "unknown";
		}
		return province;
	}

}
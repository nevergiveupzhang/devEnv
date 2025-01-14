package cn.cnnic.report.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.cnnic.report.config.model.IndexConfigModel;
import cn.cnnic.report.config.model.ReportApplicationConfigModel;
import cn.cnnic.report.service.ReportService;
import cn.cnnic.report.service.ServiceResponse;
import cn.cnnic.report.utils.DataUtil;
import cn.cnnic.report.vo.ReportDailyVO;

@Controller
@RequestMapping("/")
public class ReportController {
	
	@Autowired
	private ReportService reportService;
	@Autowired
	private ReportApplicationConfigModel config;
	@RequestMapping("/")
	public ModelAndView index() {
		List<IndexConfigModel> model=config.getIndexes();
		return new ModelAndView("index","model",model);
	}

	
	@RequestMapping(value = "/generate", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> generate(@RequestParam String indexName, @RequestParam String reportDate, HttpServletRequest request)
			throws IOException {
		long startTime=System.currentTimeMillis();
		ServiceResponse serviceResponse=reportService.generateReport(indexName, reportDate);
		Map<String,Object> resultMap=new LinkedHashMap<String,Object>();
		resultMap.put("success", "true");
		resultMap.put("code", serviceResponse.getCode());
		resultMap.put("data", serviceResponse.getData());
		resultMap.put("took", System.currentTimeMillis()-startTime);
		return resultMap;
	}

	@RequestMapping(value = "/fetch", method = RequestMethod.GET)
	public ResponseEntity<byte[]> fetch(@RequestParam String q, HttpServletRequest request) throws IOException {
		int posParentheses = q.indexOf('(');
		int posAntiparentheses = q.indexOf(')');
		String indexName = q.substring(0, posParentheses);
		String reportDate = q.substring(posParentheses + 1, posAntiparentheses);
		return reportService.fetchReportFile(indexName,reportDate);
	}
	
	@RequestMapping(value = "/graph", method = RequestMethod.GET)
	public ModelAndView graph(@RequestParam String q, HttpServletRequest request) throws IOException {
		int posParentheses = q.indexOf('(');
		int posAntiparentheses = q.indexOf(')');
		String indexName = q.substring(0, posParentheses);
		String reportDate = q.substring(posParentheses + 1, posAntiparentheses);
		ReportDailyVO model=reportService.dailyCount(indexName,reportDate);
		return new ModelAndView("graph","model",DataUtil.toJsonStr(model));
	}
	
	@RequestMapping(value = "/deleteFile", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> deleteFile(@RequestParam String q, HttpServletRequest request) throws IOException {
		int posParentheses = q.indexOf('(');
		int posAntiparentheses = q.indexOf(')');
		String indexName = q.substring(0, posParentheses);
		String reportDate = q.substring(posParentheses + 1, posAntiparentheses);
		boolean isExists=reportService.deleteFile(indexName,reportDate);
		Map<String,Object> resultMap=new LinkedHashMap<String,Object>();
		resultMap.put("success", "true");
		if(isExists) {
			resultMap.put("code", ServiceResponse.OK);
		}else {
			resultMap.put("code", ServiceResponse.NOT_FOUND);
		}
		return resultMap;
	}
	
}

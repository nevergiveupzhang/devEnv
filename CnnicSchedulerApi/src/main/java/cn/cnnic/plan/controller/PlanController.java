package cn.cnnic.plan.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;

import cn.cnnic.plan.model.PlanModel;
import cn.cnnic.plan.service.PlanService;
import cn.cnnic.plan.utils.DataUtil;
import cn.cnnic.plan.utils.RequestUtil;

@Controller
@RequestMapping("/v1/")
public class PlanController {
	private static final Logger LOGGER=LoggerFactory.getLogger(PlanController.class);
	@Autowired
	private PlanService planService;
	
	@RequestMapping(path="/plan/{person}",method=RequestMethod.GET)
	@ResponseBody
	public String plan(@PathVariable String person,HttpServletRequest request) throws JsonProcessingException, ParseException, UnsupportedEncodingException {
		String fromDay=request.getParameter("fromDay");
		String toDay=request.getParameter("toDay");
		String planType=request.getParameter("type");
		Map<String,Object> resultMap=new LinkedHashMap<String,Object>();
		List<PlanModel> planList=planService.queryPlans(person,fromDay,toDay,planType);
		resultMap.put("code", HttpStatus.OK.value());
		resultMap.put("success", "true");
		resultMap.put("data", planList);
		return DataUtil.toJsonStr(resultMap);
	}
	
	@RequestMapping(path="/plans",method=RequestMethod.GET)
	@ResponseBody
	public String plans(HttpServletRequest request) throws ParseException, JsonProcessingException, UnsupportedEncodingException {
		LOGGER.info(RequestUtil.getIpAddr(request));
		String person=request.getParameter("person");
		String fromDay=request.getParameter("fromDay");
		String toDay=request.getParameter("toDay");
		String planType=request.getParameter("type");
		Map<String,Object> resultMap=new HashMap<String,Object>();
		List<PlanModel> planList=planService.queryPlans(person,fromDay,toDay,planType);
		resultMap.put("code", HttpStatus.OK.value());
		resultMap.put("success", "true");
		resultMap.put("data", planList);
		return DataUtil.toJsonStr(resultMap);
	}
}

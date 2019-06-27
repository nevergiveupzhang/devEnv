package cn.cnnic.plan.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;

import cn.cnnic.plan.model.PersonModel;
import cn.cnnic.plan.service.PersonService;
import cn.cnnic.plan.utils.DataUtil;
import cn.cnnic.plan.utils.RequestUtil;

@Controller
@RequestMapping("/v1/")
public class PersonController {
	@Autowired
	private PersonService personService;
	
	@RequestMapping(path="/person/{userProfile}",method=RequestMethod.GET)
	@ResponseBody
	public String person(@PathVariable String userProfile) throws JsonProcessingException {
		Map<String,Object> resultMap=new LinkedHashMap<String,Object>();
		List<PersonModel> personList=personService.queryPersons(userProfile);
		resultMap.put("code", HttpStatus.OK.value());
		resultMap.put("success", "true");
		resultMap.put("data", personList);
		return DataUtil.toJsonStr(resultMap);
	}
	@RequestMapping(path="/persons",method=RequestMethod.GET)
	@ResponseBody
	public String persons(HttpServletRequest request) throws ParseException, JsonProcessingException, UnsupportedEncodingException {
		String id=RequestUtil.getQueryParam(request, "id");
		String userName=RequestUtil.getQueryParam(request, "name");
		Map<String,Object> resultMap=new LinkedHashMap<String,Object>();
		List<PersonModel> personList=personService.queryPersons(id,userName);
		resultMap.put("code", HttpStatus.OK.value());
		resultMap.put("success", "true");
		resultMap.put("data", personList);
		return DataUtil.toJsonStr(resultMap);
	}
}

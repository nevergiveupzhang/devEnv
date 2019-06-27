package cn.cnnic.whoistool.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;

import cn.cnnic.whoistool.common.exception.NullParamException;
import cn.cnnic.whoistool.common.web.WebConstants;
import cn.cnnic.whoistool.dto.DomainDTO;
import cn.cnnic.whoistool.service.IWhoisAuditService;
import cn.cnnic.whoistool.service.IWhoisService;

/*
 * @author zhangtao
 * @since 2017-06-24
 */
@Controller
@RequestMapping(value = WebConstants.ROOT_PATH)
public class WhoisController {
	private static final Logger LOGGER = LoggerFactory.getLogger(WhoisController.class);

	@Resource(name = "whoisService")
	private IWhoisService whoisService;
	@Resource(name = "whoisAuditService")
	private IWhoisAuditService whoisAuditService;

	/**
	 * 
	 *
	 * @param
	 * @return
	 * @throws NullParamException
	 */
	@RequestMapping(value = WebConstants.QUERY, method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String query(HttpServletRequest req, @RequestParam(value = "domains[]") String[] domains,
			@RequestParam(value = "whoisDatas[]") String[] whoisDatas) throws NullParamException {
		if (null == domains || null == whoisDatas || 0 == domains.length || 0 == whoisDatas.length) {
			return null;
		}
		String ipAddr = req.getRemoteAddr();
		LOGGER.info("query ip is ["+ipAddr+"]");
		StringBuilder sb=new StringBuilder();
		for(String domain:domains){
			sb.append(domain+",");
		}
		LOGGER.info("query domains are ["+sb.toString()+"]");
		sb=new StringBuilder();
		for(String data:whoisDatas){
			sb.append(data+",");
		}
		LOGGER.info("query fields are ["+sb.toString()+"]");
		//whoisAuditService.insert(ipAddr, domains);
		List<DomainDTO> domainList = whoisService.query(domains, whoisDatas);
		return JSONObject.toJSONString(domainList);
	}
}

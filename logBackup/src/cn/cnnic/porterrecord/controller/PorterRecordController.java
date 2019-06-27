package cn.cnnic.porterrecord.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

import cn.cnnic.porterrecord.common.exception.NullParamException;
import cn.cnnic.porterrecord.common.web.WebConstants;
import cn.cnnic.porterrecord.dto.PorterRecordDTO;
import cn.cnnic.porterrecord.po.ServicePO;
import cn.cnnic.porterrecord.service.IPorterRecordService;
import cn.cnnic.porterrecord.vo.PorterRecordVO;

/*
 * @author zhangtao
 * @since 2017-05-22
 */
@Controller
@RequestMapping(value = WebConstants.ROOT_PATH)
public class PorterRecordController {
	private static final Logger LOGGER = LoggerFactory.getLogger(PorterRecordController.class);
	@Resource(name="porterRecordService")
	private IPorterRecordService porterRecordService;
	
	
	/**
     *  添加一条porterrecord记录
     *
     * @param 
     * @return
	 * @throws NullParamException 
     */
    @RequestMapping(value = WebConstants.ADD_PORTER_RECORD, method = RequestMethod.GET)
    @ResponseBody
    public String addPorterRecord(HttpServletRequest request) throws NullParamException{
    	String serviceName=request.getParameter("serviceName");
    	String ipAddr=request.getRemoteAddr();
    	String taskName=request.getParameter("taskName");
    	if(null==serviceName||"".equals(serviceName)||null==taskName||"".equals(taskName)){
    		throw new NullParamException("请求参数serviceName和taskName不能为空！");
    	}
    	PorterRecordVO porterRecord=new PorterRecordVO();
    	porterRecord.setTaskName(taskName);
    	porterRecord.setIpAddr(ipAddr);
    	porterRecord.setServiceName(serviceName);
		return porterRecordService.addPorterRecord(porterRecord);
    }
    
    /**
     * 根据日期查询porterrecord
     *
     * @param request
     * @return 
     * @throws ParseException 
     */
    @RequestMapping(value = WebConstants.QUERY_PORTER_RECORD_BY_DATE, method = RequestMethod.GET)
    public ModelAndView getPorterRecordByDate(HttpServletRequest request) throws ParseException{
    	String startDateStr=request.getParameter("startDate");
    	String endDateStr=request.getParameter("endDate");
    	
    	Date startDate=null;
    	Date endDate=null;
    	
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    	TimeZone gmtTime = TimeZone.getTimeZone("GMT");
    	sdf.setTimeZone(gmtTime);
    	
    	if(null==startDateStr||"".equals(startDateStr)){
    		startDate=new Date();
    		endDate=startDate;
    	}else if(null==endDateStr||"".equals(endDateStr)){
    		startDate=sdf.parse(startDateStr);
    		endDate=startDate;
    	}else{
    		startDate=sdf.parse(startDateStr);
    		endDate=sdf.parse(endDateStr);
    	}
    	LOGGER.info("PorterRecordController->getPorterRecordByDate startDateStr="+startDateStr+"&endDateStr="+endDateStr);
    	LOGGER.info("PorterRecordController->getPorterRecordByDate startDate="+startDate+"&endDate="+endDate);
    	
    	List<PorterRecordDTO> resultList=porterRecordService.getPorterRecordByDate(startDate,endDate);
    	for(PorterRecordDTO po:resultList){
    		LOGGER.info(po.toString());
    	}
    	Map<String,Object> model=new HashMap<String,Object>();
    	model.put("data", resultList);
    	model.put("startDate", sdf.format(startDate));
    	model.put("endDate", sdf.format(endDate));
    	return new ModelAndView("porterRecordList","model",model);
    }
    
    @RequestMapping(value=WebConstants.EDIT_SERVICE,method=RequestMethod.GET)
    public ModelAndView getAllServices(){
    	List<ServicePO> serviceList=porterRecordService.getAllServices();
    	Map<String,Object> model=new HashMap<String,Object>();
    	model.put("data", serviceList);
    	return new ModelAndView("serviceList","model",model);
    }
    
    @RequestMapping(value=WebConstants.UPDATE_SERVICE,method=RequestMethod.GET)
    @ResponseBody
    public Integer updateService(HttpServletRequest request){
    	Integer serviceId=Integer.parseInt(request.getParameter("id"));
    	String serviceName=request.getParameter("serviceName");
    	String ipAddr=request.getParameter("ipAddr");
    	ServicePO service=new ServicePO();
    	service.setId(serviceId);
    	service.setServiceName(serviceName);
    	service.setIpAddr(ipAddr);
    	
    	return porterRecordService.updateService(service);
    }
    

    @RequestMapping(value=WebConstants.DELETE_SERVICE,method=RequestMethod.GET)
    @ResponseBody
    public Integer deleteService(HttpServletRequest request){
    	Integer serviceId=Integer.parseInt(request.getParameter("id"));
    	return porterRecordService.deleteService(serviceId);
    }
    
    @RequestMapping(value=WebConstants.SHOW_CHART,method=RequestMethod.GET)
    @ResponseBody
    public ModelAndView showChart(HttpServletRequest request) throws ParseException{
    	Integer serviceId=Integer.parseInt(request.getParameter("id"));
    	String serviceName=request.getParameter("serviceName");
    	String startDateStr=request.getParameter("startDate");
    	String endDateStr=request.getParameter("endDate");
    	
    	Date startDate=null;
    	Date endDate=null;
    	
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    	TimeZone gmtTime = TimeZone.getTimeZone("GMT");
    	sdf.setTimeZone(gmtTime);
    	
    	if(null==startDateStr||"".equals(startDateStr)){
    		startDate=new Date();
    		endDate=startDate;
    	}else if(null==endDateStr||"".equals(endDateStr)){
    		startDate=sdf.parse(startDateStr);
    		endDate=startDate;
    	}else{
    		startDate=sdf.parse(startDateStr);
    		endDate=sdf.parse(endDateStr);
    	}
    	Map<String,Map<String,Integer>> result=porterRecordService.getPorterRecordByServiceId(serviceId,startDate,endDate);
    	Map<String,Object> model=new HashMap<String,Object>();
    	LOGGER.debug("result json",JSONObject.toJSONString(result));
    	model.put("serviceName",serviceName);
    	model.put("data", result);
    	return new ModelAndView("showChart","model",JSONObject.toJSONString(model));
    }
    
    /**
     *  添加一条service
     *
     * @param 
     * @return
	 * @throws NullParamException 
     */
    @RequestMapping(value = WebConstants.ADD_SERVICE, method = RequestMethod.GET)
    @ResponseBody
    public String addService(HttpServletRequest request) throws NullParamException{
    	String serviceName=request.getParameter("serviceName");
    	String ipAddr=request.getParameter("ipAddr");
    	if(null==serviceName||"".equals(serviceName)||null==ipAddr||"".equals(ipAddr)){
    		throw new NullParamException("请求参数serviceName不能为空！");
    	}
    	ServicePO service=new ServicePO();
    	service.setServiceName(serviceName);
    	service.setIpAddr(ipAddr);
		return porterRecordService.addService(service);
    }
}

package cn.cnnic.plan.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cnnic.plan.exception.InvalidParamException;
import cn.cnnic.plan.mapper.PlanMapper;
import cn.cnnic.plan.model.PlanModel;
import cn.cnnic.plan.utils.CalendarUtil;
import cn.cnnic.plan.utils.StringUtil;

@Service
public class PlanService {
	private final static Logger LOGGER=LoggerFactory.getLogger(PlanService.class);
	@Autowired
	PlanMapper planMapper;

	public List<PlanModel> queryPlans(String person, String fromDay, String toDay, String planTypeStr) throws ParseException {
		LOGGER.info("userProfile="+person+"&fromDay="+fromDay+"&toDay="+toDay+"&planType="+planTypeStr);		
		String personId = null,personName = null;
		if(StringUtil.isEmpty(person)) {}
		else if(StringUtil.isNumberic(person)) {
			personId=person;
		}else {
			personName=person;
		}
		return queryPlans(personId,personName,fromDay,toDay,planTypeStr);
	}
	
	public List<PlanModel> queryPlans(String personId,String personName, String fromDay, String toDay,String planTypeStr) throws ParseException{
		String today=CalendarUtil.getTodayDate();
		if(StringUtil.isEmpty(fromDay)&&StringUtil.isEmpty(toDay)) {
			fromDay=CalendarUtil.getIntervalDate(new Date(), -30);
			toDay=today;
		}else if(StringUtil.isNotEmpty(fromDay)&&StringUtil.isNotEmpty(toDay)) {
			if(CalendarUtil.getDateInterval(fromDay, toDay)<0) {
				throw new InvalidParamException("toDay cannot be ealier than fromDay");
			}
		}else if(StringUtil.isEmpty(fromDay)&&StringUtil.isNotEmpty(toDay)) {
			throw new InvalidParamException("parameter fromDay cannot be empty while toDay is defined");
		}else if(StringUtil.isNotEmpty(fromDay)&&StringUtil.isEmpty(toDay)) {
			if(CalendarUtil.getDateInterval(fromDay, today)<0) {
				throw new InvalidParamException("fromDay cannot be later than today");
			}else {
				toDay=today;
			}
		}
		String []planType = null;
		if(StringUtil.isNotEmpty(planTypeStr)) {
			planType=planTypeStr.split(",");
		}
		return planMapper.queryPlans(personId,personName,fromDay,toDay,planType);
	}
}

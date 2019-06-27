package cn.cnnic.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import cn.cnnic.common.QueryFilter;
import cn.cnnic.model.ScheduleLog;
import cn.cnnic.utils.CalendarUtil;
import cn.cnnic.utils.DataUtils;

public class ScheduleLogController extends Controller {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleLogController.class);
	
	@Before({POST.class})
	public void getRecent30Days() {
		String thirtyDaysBeforeDate=CalendarUtil.getIntervalDate(new Date(), -30);
		QueryFilter queryFilter=new QueryFilter();
		queryFilter.setWhereString("log_date>='"+thirtyDaysBeforeDate+"'");
		queryFilter.setOrderString("log_date desc");
		List<ScheduleLog> list=ScheduleLog.me.getEntityList(queryFilter);
		String result = DataUtils.listToJsonStr(list, ScheduleLog.me);
	   renderJson(result);
	}
}

package cn.cnnic.controller;

import cn.cnnic.common.DataService;
import cn.cnnic.common.QueryFilter;
import cn.cnnic.common.UpdateFilter;
import cn.cnnic.model.GroupPerson;
import cn.cnnic.model.Scheduler;
import cn.cnnic.utils.CalendarUtil;
import cn.cnnic.utils.DataUtils;
import cn.cnnic.utils.EmailUtil;
import cn.cnnic.utils.RequestUtil;
import cn.cnnic.utils.SmsUtil;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.awt.image.DataBufferByte;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedulerController extends Controller {
	private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerController.class);
	public DataService dataService = new DataService();

	public void index() {
	}

	public void getGroupPersonList() {
		QueryFilter queryFilter = new QueryFilter();
		queryFilter.setWhereString("gid<>1 and personStatus<>0");
		queryFilter.setSelectFields("*");
		queryFilter.setOrderString("gid desc");
		LOGGER.info("--------------------[SchedulerController->getGroupPersonList]\t\t" + queryFilter.toString());
		List<GroupPerson> dictList = GroupPerson.me.getEntityList(queryFilter);
		String result = DataUtils.listToJsonStr(dictList, GroupPerson.me);
		renderJson(result);
	}

	public void getSchedulerList() {
		QueryFilter queryFilter = new QueryFilter();

		String whereString = getPara("whereString");
		if (whereString == null) {
			whereString = "1=1";
		}
		queryFilter.setWhereString(whereString);
		queryFilter.setSelectFields("*");
		queryFilter.setOrderString("name ASC");
		LOGGER.info("--------------------[SchedulerController->getSchedulerList]\t\t" + queryFilter.toString());
		List<Scheduler> dictList = Scheduler.me.getEntityList(queryFilter);
		String result = DataUtils.listToJsonStr(dictList, Scheduler.me);
		renderJson(result);
	}

	@Before({ POST.class })
	public void deleteSchedule() {
		int result = 0;
		String whereString = getPara("whereString");
		LOGGER.info("--------------------[SchedulerController->deleteSchedule]\t\t" + whereString);
		try {
			LOGGER.info("--------------------[SchedulerController->deleteSchedule] BEGIN TO SEND EMAIL TO zhangtao@cnnic.cn");
			LOGGER.info("--------------------[SchedulerController->updateSchedule] remote addr is "+getRequest().getRemoteAddr());
			EmailUtil.sendEmail(new String[] { "zhangtao@cnnic.cn" }, "排班表修改提醒", "提醒：有人修改了排班表！对方IP为："+getRequest().getRemoteAddr()+"。详情："+whereString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		result = this.dataService.del("gc_schedule_scheduler", whereString);
		renderJson(Integer.valueOf(result));
	}

	/*
	 * @return -1 不在排班周期；-2 修改前后的班次一样，未发生实际变更；>=0 变更的记录数
	 */
	@Before({ POST.class })
	public void updateSchedule() {
		int result = 0;
		int pid=Integer.valueOf(getPara("pid"));
		String pname=getPara("pname");
		String eventsDate=getPara("eventsDate");
		String newEvents=getPara("events");
		String originalEvents="";
		Record record=Db.findFirst("select * from gc_schedule_scheduler where pid=? and day=?",pid,eventsDate);
		if(null!=record) {
			originalEvents=record.getStr("events");
		}
		if("".equals(originalEvents)) {
			renderJson(-1);
			return;
		}
		if(originalEvents.equals(newEvents)) {
			renderJson(-2);
			return;
		}
		LOGGER.info("--------------------[SchedulerController->updateSchedule]\t\t" + "pid="+pid+";pname="+pname+";day="+eventsDate+";events="+newEvents+";originalEvents="+originalEvents);
		result = Db.update("update gc_schedule_scheduler set events=? where pid=? and day=?",newEvents,pid,eventsDate);
		if(0!=result) {
			Db.update("insert into gc_schedule_log(person_id,person_name,events_date,from_events,to_events,log_date) values(?,?,?,?,?,?)",pid,pname,eventsDate,originalEvents,newEvents,CalendarUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
		}
		try {
			LOGGER.info("--------------------[SchedulerController->updateSchedule] BEGIN TO SEND EMAIL TO zhangtao@cnnic.cn and xuxiaocheng@cnnic.cn");
			LOGGER.info("--------------------[SchedulerController->updateSchedule] remote addr is "+RequestUtil.getIpAddr(getRequest()));
			EmailUtil.sendEmail(new String[] { "zhangtao@cnnic.cn"}, "排班表修改提醒", "提醒：有人修改了排班表！对方IP为："+RequestUtil.getIpAddr(getRequest())+"。详情："+"pid="+pid+"&&pname="+pname+"&&day="+eventsDate+"&&toEvents="+newEvents+"&&fromEvents="+originalEvents);
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderJson(Integer.valueOf(result));
	}

	@Before({ POST.class })
	public void saveSchedule() {
		String insertedJson = getPara("inserted");
		LOGGER.info("--------------------[SchedulerController->saveSchedule]\t\tinsertedJson:" + insertedJson);
		boolean result = this.dataService.save(insertedJson, Scheduler.class);
		renderJson(Boolean.valueOf(result));
	}
}

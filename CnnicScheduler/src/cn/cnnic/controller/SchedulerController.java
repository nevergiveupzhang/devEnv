package cn.cnnic.controller;

import cn.cnnic.common.QueryFilter;
import cn.cnnic.model.GroupPerson;
import cn.cnnic.model.Scheduler;
import cn.cnnic.utils.CalendarUtil;
import cn.cnnic.utils.DataUtils;
import cn.cnnic.utils.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedulerController extends Controller {
	private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerController.class);

	public void index() {
	}

	public void getGroupPersonList() {
		String groupName = getPara("groupName");
		QueryFilter queryFilter = new QueryFilter();
		if (null != groupName && !"".equals(groupName)) {
			queryFilter.setWhereString("groupName='" + groupName + "'");
		} else {
			queryFilter.setWhereString("personStatus<>0 and groupStatus<>0");
		}
		queryFilter.setSelectFields("*");
		queryFilter.setOrderString("`order` desc");
		List<GroupPerson> dictList = GroupPerson.me.getEntityList(queryFilter);
		String result = DataUtils.listToJsonStr(dictList, GroupPerson.me);
		LOGGER.debug(result);
		renderJson(result);
	}

	@Before({POST.class})
	public void getSchedulerList() {
		LOGGER.info("--------------------[SchedulerController->getSchedulerList]	begin to get scheduler list");
		QueryFilter queryFilter = new QueryFilter();
		String startDate = getPara("startDate");
		String endDate = getPara("endDate");
		String pid = getPara("pid");
		String events = getPara("events");
		String eventList[] = getParaValues("eventList[]");

		String whereString = "day>='" + startDate+"'";
		if ((startDate == null) || ("".equals(startDate))) {
			renderJson("");
			return;
		}
		if(!CalendarUtil.isValidDate(startDate,"yyyy-MM-dd")) {
			renderJson("");
			return;
		}
		if ((endDate != null) && (!"".equals(endDate))&&CalendarUtil.isValidDate(endDate,"yyyy-MM-dd")) {
			whereString = whereString + " and day<'" + endDate + "'";
		}
		if (null != eventList && eventList.length!=0) {
			whereString = whereString + " and events in (" + buildEventList(eventList) + ")";
		}
		if ((pid != null) && (!"".equals(pid))) {
			whereString = whereString + " and pid=" + Integer.valueOf(pid);
		}
		if ((events != null) && (!"".equals(events))&&StringUtils.getChineseNum(events)==events.length()&&events.length()<=4) {
			whereString = whereString + " and events='" + events + "'";
		}

		whereString = whereString + " and pid in(" + buildNoneSchedulerPersonIDs() + ")";

		queryFilter.setWhereString(whereString);
		queryFilter.setSelectFields("*");
		queryFilter.setOrderString("name ASC");
		LOGGER.info("--------------------[SchedulerController->getSchedulerList]\t\t" + queryFilter.toString());
		List<Scheduler> schedulerList = Scheduler.me.getEntityList(queryFilter);
		String result = DataUtils.listToJsonStr(schedulerList, Scheduler.me);
		renderJson(result);
		LOGGER.info("--------------------[SchedulerController->getSchedulerList]	getting scheduler list finished");
	}

	private String buildEventList(String[] eventList) {
		String resultStr="";
		int i=0;
		for(String event:eventList) {
			if(StringUtils.getChineseNum(event)!=event.length()||event.length()>4) {
				continue;
			}
			if(i==0) {
				resultStr=resultStr+"'"+event+"'";
			}else {
				resultStr=resultStr+",'"+event+"'";
			}
			i++;
		}
		return resultStr;
	}

	private String buildNoneSchedulerPersonIDs() {
		QueryFilter queryFilter = new QueryFilter();
		queryFilter.setWhereString("personStatus<>0 and groupStatus<>0");
		queryFilter.setSelectFields("pid");
		List<GroupPerson> groupPersonList = GroupPerson.me.getEntityList(queryFilter);
		int i = 0;
		String result = "";
		for (GroupPerson gp : groupPersonList) {
			Integer pid = gp.getInt("pid");
			if (i == 0) {
				result += pid;
			} else {
				result += "," + pid;
			}
			i++;
		}
		return result;
	}
	
	@Before({POST.class})
	public void exportMonthScheduler() {
		String year=getPara("year");
		String month=getPara("month");
		if(null==year||month==year||"".equals(year)||"".equals(month)) {
			renderText("请传递正确参数！");
			return;
		}
		
		String startDate=year+"-"+month+"-01";
		String endDate=CalendarUtil.getFirstDayOfNextMonth(year+"-"+month+"-01");
		File exportMonthSchedulerFile=new File(getSession().getServletContext().getRealPath(File.separator) +"/export/"+year+"-"+month+".txt");
		LOGGER.info(exportMonthSchedulerFile.getAbsolutePath());
		BufferedWriter bw=null;
		try {
			exportMonthSchedulerFile.createNewFile();
			bw=new BufferedWriter(new FileWriter(exportMonthSchedulerFile));
			bw.write("日期\t\t白班\t\t夜班\t\n");
		} catch (IOException e) {
			e.printStackTrace();
			renderText("文件异常！");
			return;
		}
		while(CalendarUtil.getDateInterval(startDate, endDate)!=0) {
			List<Record> dayRecordList=Db.find("select * from gc_schedule_scheduler where day=? and events='白班'",startDate);
			List<Record> nightRecordList=Db.find("select * from gc_schedule_scheduler where day=? and (events='夜班' or events='白连夜')",startDate);
			String dayNameStr="未指定\t",nightNameStr="未指定";
			if(null!=dayRecordList&&!dayRecordList.isEmpty()) {
				for(int i=0;i<dayRecordList.size();i++) {
					Record record=dayRecordList.get(i);
					String name=record.getStr("name");
					if(i==0) {
						dayNameStr=name;
					}else {
						dayNameStr+=","+name;
					}
				}
			}
			LOGGER.info(startDate+" 白班:"+dayNameStr);
			if(null!=nightRecordList&&!nightRecordList.isEmpty()) {
				for(int i=0;i<nightRecordList.size();i++) {
					Record record=nightRecordList.get(i);
					String name=record.getStr("name");
					if(i==0) {
						nightNameStr=name;
					}else {
						nightNameStr+=","+name;
					}
				}
			}
			LOGGER.info(startDate+" 夜班:"+nightNameStr);
			try {
				bw.write(startDate+"\t"+dayNameStr+"\t"+nightNameStr+"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			startDate=CalendarUtil.getIntervalDate(startDate, 1);
		}
		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		renderFile(exportMonthSchedulerFile);
	}
}

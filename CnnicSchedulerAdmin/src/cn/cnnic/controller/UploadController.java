package cn.cnnic.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import cn.cnnic.common.QueryFilter;
import cn.cnnic.model.GroupPerson;
import cn.cnnic.model.Role;
import cn.cnnic.model.Scheduler;
import cn.cnnic.model.SecondLine;
import cn.cnnic.utils.CalendarUtil;

import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * 1、插入空记录
 * 2、安排节假日的休息
 * 3、安排休假
 * 4、保存xml，安排所有夜班和白班
 * 5、安排所有夜班和周末白班的换休以及周末休息
 * 6、安排加班的常白
 * 7、安排剩余的常白
 * 8、安排二线运维
 * @author zhangtao xuxiaocheng
 * @since 2019-01-05
 */
public class UploadController extends Controller {
	private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

	public void index() {
		renderText("upload page");
	}

	public void upXml() {
		String xml = getPara("xml");
		String startDate = getPara("startDate");
		String endDate = getPara("endDate");
		String containsFestival = getPara("containsFestival");
		String festivalStartDate = getPara("festivalStartDate");
		String festivalEndDate = getPara("festivalEndDate");
		String containsOvertime = getPara("containsOvertime");
		String overtimeStartDate = getPara("overtimeStartDate");
		String overtimeEndDate = getPara("overtimeEndDate");
		String containsVocation=getPara("containsVocation");
		String vocationText=getPara("vocationText");
		if (!isValidParam(startDate, endDate, containsFestival, festivalStartDate, festivalEndDate, containsOvertime,
				overtimeStartDate, overtimeEndDate)) {
			return;
		}
		if (!isDateMatch(startDate, endDate, xml)) {
			renderText("开始日期和结束日期与xml日期不匹配！");
			return;
		}
		if(isArranged(startDate,endDate)) {
			renderText("此期间已经安排有班次，请先删除再导入！");
			return;
		}
		Set<String>  newPersonSet=checkNewPersons(xml);
		if(newPersonSet.size()!=0) {
			renderText("此期间存在新人，请先添加人员再导入数据！新人员有："+buildStringFromSet(newPersonSet,","));
			return;
		}
		insertNullEventsSchedulerRecord(startDate, endDate);
		if ("Y".equals(containsFestival)) {
			arrangeFestival(festivalStartDate, festivalEndDate);
		}
		if("Y".equals(containsVocation)){
			arrangeVocation(vocationText);
		}
		saveXmlRecord(xml);
		arrangeRest(startDate, endDate, containsFestival, festivalStartDate, festivalEndDate, containsOvertime,
				overtimeStartDate, overtimeEndDate);
		if ("Y".equals(containsOvertime)) {
			arrangeOvertime(overtimeStartDate, overtimeEndDate);
		}
		arrangeNormal(startDate, endDate);
		arrangeSecondline(startDate, endDate);
		renderText("已成功上传" + startDate + "至" + endDate + "之间的数据！");
	}
	
	/*从Set集合创建字符串*/
	private String buildStringFromSet(Set<String> dataSet, String split) {
		String result="";
		int idx=0;
		for(String data:dataSet) {
			if(idx==0) {
				result+=data;
			}else {
				result+=split+data;
			}
			idx++;
		}
		return result;
	}

	/*
	 * 检查是否有新人员
	 */
	private Set<String> checkNewPersons(String xml) {
		Set<String> xmlResultSet=new HashSet<String>();
		// parse XML
		Document document = null;
		try {
			document = DocumentHelper.parseText(xml);
			Element root = document.getRootElement();
			// iterate through child elements of root
			for (Iterator<Element> assignIt = root.elementIterator(); assignIt.hasNext();) {
				Element assignment = assignIt.next();
				if (assignment.getName().equals("Assignment")) {
					String name = "";
					int idx=0;
					for (Iterator<Element> it = assignment.elementIterator(); it.hasNext();) {
						Element e = it.next();
						if (e.getName().equals("Employee")) {
							name = e.getText();
						}
					}
					LOGGER.info("UploadController-->checkNewPersons	" + name);
					Record personRecord=Db.findFirst("select * from gc_schedule_group_person_v where name=?",name);
					if(null==personRecord) {
						xmlResultSet.add(name);
					}
				}
			}
			return xmlResultSet;
		} catch (Exception ex) {
			ex.printStackTrace();
			renderText("数据失败！");
			return new HashSet<String>();
		}

	}
	private boolean isArranged(String startDate, String endDate) {
		Record record=Db.findFirst("select * from gc_schedule_scheduler where day>='"+startDate+"' and day<='"+endDate+"'" );
		if(null!=record) {
			return true;
		}else {
			return false;
		}
	}

	private void arrangeVocation(String vocationText) {
		String []lines=vocationText.split("\n");
		for(String line:lines) {
			String []item=line.split(" ");
			String name=item[0];
			String startDate=CalendarUtil.convertFormat(item[1].split("-")[0],"yyyyMMdd","yyyy-MM-dd");
			String endDate=CalendarUtil.convertFormat(item[1].split("-")[1],"yyyyMMdd","yyyy-MM-dd");
			Db.update("update gc_schedule_scheduler set events='休假' where name=? and day>=? and day<=?",name,startDate,endDate);
		}
	}

	private void arrangeSecondline(String startDate, String endDate) {
		String[] groupLeaderArr = buildRoleItemArr("GROUPLEADER");
		String[] dbaArr = buildRoleItemArr("DBA");
		String[] networkArr = buildRoleItemArr("NETWORK");
		String[] dnsArr = buildRoleItemArr("DNS");
		String[] sos2Arr = buildRoleItemArr("SOS2");
		String[] eberoArr = buildRoleItemArr("EBERO");
		String[] sdnsArr = buildRoleItemArr("SDNS");
		String lastGroupLeader = "", lastDba = "", lastNetwork = "", lastDns = "", lastSos2 = "", lastEbero = "",
				lastSdns = "";
		Record record = Db.findFirst("select * from gc_schedule_secondline where startDate=?",
				CalendarUtil.getIntervalDate(startDate, -7));
		//如果上一次二线运维不存在，那就以这次为第一次来安排
		if (null == record) {
			lastGroupLeader=groupLeaderArr[groupLeaderArr.length-1];
			lastDba=dbaArr[dbaArr.length-1];
			lastNetwork=networkArr[networkArr.length-1];
			lastDns=dnsArr[dnsArr.length-1];
			lastSos2=sos2Arr[sos2Arr.length-1];
			lastSdns=sdnsArr[sdnsArr.length-1];
			lastEbero=eberoArr[eberoArr.length-1];
		} else {
			lastGroupLeader = record.getStr("groupLeader");
			lastDba = record.getStr("dba");
			lastNetwork = record.getStr("network");
			lastDns = record.getStr("cn");
			lastSos2 = record.getStr("sos2");
			lastSdns = record.getStr("sdns");
			lastEbero=record.getStr("ebero");
		}
		String referDate = startDate;
		while (CalendarUtil.getDateInterval(referDate, endDate) > 0) {
			String nextGroupLeader = buildNextRoleItem(groupLeaderArr, lastGroupLeader);
			String nextDba = buildNextRoleItem(dbaArr, lastDba);
			String nextNetwork = buildNextRoleItem(networkArr, lastNetwork);
			String nextDns = buildNextRoleItem(dnsArr, lastDns);
			String nextSos2 = buildNextRoleItem(sos2Arr, lastSos2);
			String nextEbero = buildNextEbero(eberoArr, lastEbero);
			String nextSdns = buildNextRoleItem(sdnsArr, lastSdns);
			Db.update("delete from gc_schedule_secondline where startDate=?",referDate);
			Db.update(
					"insert into gc_schedule_secondline(groupLeader,network,dba,cn,sdns,ebero,sos2,startDate)values(?,?,?,?,?,?,?,?)",
					nextGroupLeader,nextNetwork, nextDba,nextDns,nextSdns,nextEbero,nextSos2,referDate);
			LOGGER.info("UploadController->arrangeSecondline [insert into gc_schedule_secondline(groupLeader,network,dba,cn,sdns,ebero,sos2,startDate)values(?,?,?,?,?,?,?,?)]");
			LOGGER.info("UploadController->arrangeSecondline ["+nextGroupLeader+"|"+nextNetwork+"|"+nextDba+"|"+nextDns+"|"+nextSdns+"|"+nextEbero+"|"+nextSos2+"|"+referDate+"]");
			referDate=CalendarUtil.getIntervalDate(referDate, 7);
			lastGroupLeader=nextGroupLeader;
			lastDba=nextDba;
			lastNetwork=nextNetwork;
			lastDns=nextDns;
			lastSos2=nextSos2;
			lastEbero=nextEbero;
			lastSdns=nextSdns;
		}
	}

	private String buildNextEbero(String[] eberoArr, String lastEbero) {
		List<SecondLine> secondlineList=SecondLine.me.find("select * from(select * from gc_schedule_secondline order by startDate asc limit 4) as t where ebero=?",lastEbero);
		if(secondlineList.size()<4) {
			return lastEbero;
		}else {
			return buildNextRoleItem(eberoArr,lastEbero);
		}
	}

	private String buildNextRoleItem(String[] roleItemArr, String lastRoleItem) {
		int idx = 0;
		for (int i = 0; i < roleItemArr.length; i++) {
			if (lastRoleItem.equals(roleItemArr[i])) {
				idx = i;
			}
		}
		if (idx < roleItemArr.length - 1) {
			return roleItemArr[idx + 1];
		} else {
			return roleItemArr[0];
		}
	}

	private String[] buildRoleItemArr(String roleName) {
		Record record = Db.findFirst("select * from gc_schedule_role where roleName=?", roleName);
		if (null != record) {
			String roleItem = record.getStr("roleItem");
			LOGGER.info("UploadController->buildRoleItemArr[roleName="+roleName+";roleItem="+roleItem+"]");
			if (null != roleItem) {
				return roleItem.split("\\|");
			}
		}
		LOGGER.info("UploadController-->buildRoleItemArr	the item of "+roleName+" is null!");
		return new String[0];
	}

	private void arrangeNormal(String startDate, String endDate) {
		QueryFilter filter = new QueryFilter();
		filter.setSelectFields("*");
		filter.setWhereString("day>='" + startDate + "' and day<='" + endDate + "'");
		List<Scheduler> normalDaySchedulerList = Scheduler.me.getEntityList(filter);
		for (Scheduler scheduler : normalDaySchedulerList) {
			String events = scheduler.getStr("events");
			int id = scheduler.getInt("id");
			if (null == events || "".equals(events)) {
				Db.update("update gc_schedule_scheduler set events='常白' where id=?", id);
			}
		}
	}

	private void arrangeFestival(String festivalStartDate, String festivalEndDate) {
		Db.update("update gc_schedule_scheduler set events='节日休息' where day>=? and day<=?", festivalStartDate,
				festivalEndDate);
	}

	private void arrangeRest(String startDate, String endDate, String containsFestival, String festivalStartDate,
			String festivalEndDate, String containsOvertime, String overtimeStartDate, String overtimeEndDate) {
		QueryFilter filter = new QueryFilter();
		filter.setSelectFields("*");
		filter.setWhereString("day>='" + startDate + "' and day<='" + endDate + "'");
		List<Scheduler> schedulerList = Scheduler.me.getEntityList(filter);
		for (Scheduler scheduler : schedulerList) {
			String events = scheduler.getStr("events");
			String day = scheduler.getStr("day");
			int id = scheduler.getInt("id");
			int pid = scheduler.getInt("pid");

			// 节假日最后一天的夜班换休一天，其余的节假日加班均无换休,节假日前一天的夜班无换休
			if ("Y".equals(containsFestival)) {
				if (CalendarUtil.getDiffDays(day, festivalStartDate) <= 0
						&& CalendarUtil.getDiffDays(day, festivalEndDate) > 0) {
					continue;
				} else if (festivalEndDate.equals(day)) {
					if ("夜班".equals(events)) {
						directArrangeRest(pid, day, startDate, endDate, containsFestival, festivalStartDate,
								festivalEndDate, 1, "夜班换休", containsOvertime, overtimeStartDate, overtimeEndDate);
						continue;
					} else {
						continue;
					}
				} else if (CalendarUtil.getDiffDays(day, festivalStartDate) == 1) {
					continue;
				}
			}

			// 不在加班范围内的周末夜班和白班换休以及周末休息
			if (CalendarUtil.isWeekend(day) && (!"Y".equals(containsOvertime)||(CalendarUtil.getDiffDays(day, overtimeStartDate) > 0
					|| CalendarUtil.getDiffDays(day, overtimeEndDate) < 0))) {
				// 如果没有班次则安排为周末休息
				if (null == events || "".equals(events)) {
					Db.update("update gc_schedule_scheduler set events='周末休息' where id=?", id);
				}
				// 如果是白班或夜班则分别在接下来最近没有班次的工作日安排一个白班换休或两个夜班换休
				else if ("白班".equals(events)) {
					directArrangeRest(pid, day, startDate, endDate, containsFestival, festivalStartDate,
							festivalEndDate, 1, "白班换休", containsOvertime, overtimeStartDate, overtimeEndDate);
				} else if ("夜班".equals(events)) {
					directArrangeRest(pid, day, startDate, endDate, containsFestival, festivalStartDate,
							festivalEndDate, 2, "夜班换休", containsOvertime, overtimeStartDate, overtimeEndDate);
				}
			}
			// 周一至周五班次夜班换休
			else if ("夜班".equals(events)) {
				directArrangeRest(pid, day, startDate, endDate, containsFestival, festivalStartDate, festivalEndDate, 1,
						"夜班换休", containsOvertime, overtimeStartDate, overtimeEndDate);
			}
		}
	}

	private void arrangeOvertime(String overtimeStartDate, String overtimeEndDate) {
		Db.update("update gc_schedule_scheduler set events='常白' where day>=? and day<=? and events=''",
				overtimeStartDate, overtimeEndDate);
	}

	private void directArrangeRest(int pid, String currentDate, String startDate, String endDate,
			String containsFestival, String festivalStartDate, String festivalEndDate, int restDays, String events,
			String containsOvertime, String overtimeStartDate, String overtimeEndDate) {
		boolean isArranged = executeRest(pid, currentDate, endDate, containsFestival, festivalStartDate,
				festivalEndDate, restDays, 1, events, containsOvertime, overtimeStartDate, overtimeEndDate);
		if (!isArranged) {
			isArranged = executeRest(pid, currentDate, startDate, containsFestival, festivalStartDate, festivalEndDate,
					restDays, -1, events, containsOvertime, overtimeStartDate, overtimeEndDate);
		}
		if (!isArranged) {
			// TO DO
		}
	}

	private boolean executeRest(int pid, String currentDate, String criticalDate, String containsFestival,
			String festivalStartDate, String festivalEndDate, int restDays, int step, String events,
			String containsOvertime, String overtimeStartDate, String overtimeEndDate) {
		String tmpDate = currentDate;
		boolean isArranged = false;
		while (!tmpDate.equals(criticalDate)) {
			String nextDay = CalendarUtil.getIntervalDate(tmpDate, step);
			// 如果是周末将无法换休
			if (CalendarUtil.isWeekend(nextDay) && (!"Y".equals(containsOvertime)||(CalendarUtil.getDiffDays(nextDay, overtimeStartDate) > 0
					|| CalendarUtil.getDiffDays(nextDay, overtimeEndDate) < 0))) {
				tmpDate = nextDay;
				continue;
			}
			// 如果是节假日将无法换休
			if ("Y".equals(containsFestival) && CalendarUtil.getDiffDays(nextDay, festivalStartDate) <= 0
					&& CalendarUtil.getDiffDays(nextDay, festivalEndDate) >= 0) {
				tmpDate = nextDay;
				continue;
			}
			//超出排班周期无法换休
			if((step>0&&CalendarUtil.getDiffDays(criticalDate, nextDay)>0)||(step<0&&CalendarUtil.getDiffDays(criticalDate, nextDay)<0)) {
				return false;
			}
			Record record = Db.findFirst("select * from gc_schedule_scheduler where pid=? and day=?", pid, nextDay);
			String tempEvents = null;
			if (null != record) {
				tempEvents = record.getStr("events");
			}
			if (null == record || null == tempEvents || "".equals(tempEvents)) {
				Db.update("update gc_schedule_scheduler set events='" + events + "' where pid=? and day=?", pid,
						nextDay);
				restDays--;
				if (0 == restDays) {
					isArranged = true;
					break;
				}
			}
			tmpDate = nextDay;
		}
		return isArranged;
	}

	private boolean isValidParam(String startDate, String endDate, String containsFestival, String festivalStartDate,
			String festivalEndDate, String containsOvertime, String overtimeStartDate, String overtimeEndDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (null == startDate || null == endDate || "".equals(startDate) || "".equals(endDate)) {
			renderText("开始日期和结束日期不能为空！");
			return false;
		}
		if (!CalendarUtil.isValidDate(startDate, "yyyy-MM-dd")) {
			renderText("开始日期格式错误，正确格式为yyyy-MM-dd！");
			return false;
		}
		if (!CalendarUtil.isValidDate(endDate, "yyyy-MM-dd")) {
			renderText("结束日期格式错误，正确格式为yyyy-MM-dd！");
			return false;
		}
		try {
			if (sdf.parse(startDate).after(sdf.parse(endDate))) {
				renderText("结束日期不能早于开始日期！");
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if ("Y".equals(containsFestival)) {
			if (!CalendarUtil.isValidDate(festivalStartDate, "yyyy-MM-dd")) {
				renderText("节假日开始日期格式错误，正确格式为yyyy-MM-dd！");
				return false;
			}
			if (!CalendarUtil.isValidDate(festivalEndDate, "yyyy-MM-dd")) {
				renderText("节假日结束日期格式错误，正确格式为yyyy-MM-dd！");
				return false;
			}
			try {
				if (sdf.parse(festivalStartDate).after(sdf.parse(festivalEndDate))) {
					renderText("节假日结束日期不能早于节假日开始日期！");
					return false;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if ("Y".equals(containsOvertime)) {
			if (!CalendarUtil.isValidDate(overtimeStartDate, "yyyy-MM-dd")) {
				renderText("加班开始日期格式错误，正确格式为yyyy-MM-dd！");
				return false;
			}
			if (!CalendarUtil.isValidDate(overtimeEndDate, "yyyy-MM-dd")) {
				renderText("加班结束日期格式错误，正确格式为yyyy-MM-dd！");
				return false;
			}
			try {
				if (sdf.parse(overtimeStartDate).after(sdf.parse(overtimeEndDate))) {
					renderText("加班结束日期不能早于加班开始日期！");
					return false;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	/*
	 * 保存xml数据的白班和夜班
	 */
	private void saveXmlRecord(String xml) {
		String xmlResult="";
		// parse XML
		Document document = null;
		try {
			document = DocumentHelper.parseText(xml);
			Element root = document.getRootElement();
			// iterate through child elements of root
			for (Iterator<Element> assignIt = root.elementIterator(); assignIt.hasNext();) {
				Element assignment = assignIt.next();
				if (assignment.getName().equals("Assignment")) {
					String date = "";
					String name = "";
					String shift = "";
					int idx=0;
					for (Iterator<Element> it = assignment.elementIterator(); it.hasNext();) {
						Element e = it.next();
						if (e.getName().equals("Date")) {
							date = e.getText();
						} else if (e.getName().equals("Employee")) {
							name = e.getText();
						} else if (e.getName().equals("ShiftType")) {
							shift = e.getText().equals("N") ? "夜班" : "白班";
						}
					}
					LOGGER.info("UploadController-->upXml	" + date + name + shift);/////////////////////////////////////////////////////////
					// delete record if exists
					List<Record> records = Db.find("select id from gc_schedule_scheduler where name=? and day=?", name,
							date);
					if (records.size() >= 1) {
						for (int i = 0; i < records.size(); i++) {
							Db.delete("gc_schedule_scheduler", records.get(i));
						}
					}

					// check employee info then insert new record
					Record employee = Db.findFirst("select pid from gc_schedule_person where name=?", name);
					if (employee != null) {
						Db.update("insert into gc_schedule_scheduler(pid,name,day,events,createDate) values(?,?,?,?,?)",
								employee.getInt("pid"), name, date, shift,CalendarUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			renderText("数据失败！");
			return;
		}
	}

	/*
	 * 在值班表里面插入指定时间段内的值班班次记录，其中班次为空字符次串。这样的空记录为一键排班做准备。
	 */
	private void insertNullEventsSchedulerRecord(String startDate, String endDate) {
		QueryFilter queryFilter = new QueryFilter();
		queryFilter.setWhereString("personStatus<>0 and groupStatus<>0");
		queryFilter.setSelectFields("*");
		List<GroupPerson> personList = GroupPerson.me.getEntityList(queryFilter);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (GroupPerson person : personList) {
			int pid = person.getInt("pid");
			String name = person.getStr("name");
			String tmpDate = startDate;
			while (CalendarUtil.getDateInterval(tmpDate, endDate) >= 0) {
				Record scheduler = Db.findFirst("select pid from gc_schedule_scheduler where pid=? and day=?", pid,
						tmpDate);
				if (null == scheduler) {
					Db.update("insert into gc_schedule_scheduler(pid,name,day,events,createDate) values(?,?,?,?,?)", pid, name,
							tmpDate, "",CalendarUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
				}
				tmpDate = CalendarUtil.getIntervalDate(tmpDate, 1);
			}
		}
	}

	/*
	 * 判断指定的起止日期与xml数据的起止日期是否匹配
	 */
	private boolean isDateMatch(String startDate, String endDate, String xml) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date minDate = null;
		Date maxDate = null;
		try {
			minDate = sdf.parse(startDate);
			maxDate = sdf.parse(endDate);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		// parse XML
		Document document = null;
		try {
			document = DocumentHelper.parseText(xml);
			Element root = document.getRootElement();
			// iterate through child elements of root
			for (Iterator<Element> assignIt = root.elementIterator(); assignIt.hasNext();) {
				Element assignment = assignIt.next();
				if (assignment.getName().equals("Assignment")) {
					String date = "";
					for (Iterator<Element> it = assignment.elementIterator(); it.hasNext();) {
						Element e = it.next();
						if (e.getName().equals("Date")) {
							date = e.getText();
							if (minDate.after(sdf.parse(date)) || maxDate.before(sdf.parse(date))) {
								return false;
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return true;
	}
}

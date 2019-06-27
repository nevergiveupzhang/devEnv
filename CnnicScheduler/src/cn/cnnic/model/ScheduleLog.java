package cn.cnnic.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

import cn.cnnic.common.DataService;
import cn.cnnic.common.QueryFilter;

public class ScheduleLog extends Model<ScheduleLog>{
	public static final ScheduleLog me=new ScheduleLog();
	private DataService dataService = new DataService();
	
	 public List<ScheduleLog> getEntityList(QueryFilter queryFilter)
	  {
	    return (List<ScheduleLog>) this.dataService.getEntityList("gc_schedule_log", queryFilter, me);
	  }
}

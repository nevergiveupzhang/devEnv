package cn.cnnic.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

import cn.cnnic.common.DataService;
import cn.cnnic.common.QueryFilter;

@SuppressWarnings("serial")
public class SecondLine  extends Model<SecondLine>{
	public static final SecondLine me=new SecondLine();
	private DataService dataService = new DataService();
	
	public List<SecondLine> getEntityList(QueryFilter queryFilter)
	  {
	    return (List<SecondLine>) this.dataService.getEntityList("gc_schedule_secondline", queryFilter, me);
	  }
}

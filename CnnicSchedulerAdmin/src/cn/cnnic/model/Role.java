package cn.cnnic.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

import cn.cnnic.common.DataService;
import cn.cnnic.common.QueryFilter;

public class Role extends Model<Role>{
	public static final Role me=new Role();
	private DataService dataService=new DataService();
	public List<Role> find(QueryFilter qf){
		return (List<Role>) dataService.getEntityList("gc_schedule_role", qf, me);
	}
}

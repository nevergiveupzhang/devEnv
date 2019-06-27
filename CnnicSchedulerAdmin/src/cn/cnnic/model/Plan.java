package cn.cnnic.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

import cn.cnnic.common.DataService;
import cn.cnnic.common.QueryFilter;

import java.util.List;

public class Plan
  extends Model<Plan>
{
  public static final Plan me = new Plan();
  DataService dataService = new DataService();
  
  public Page<Plan> paginate(int pageNumber, int pageSize)
  {
    Page<Plan> pagePlan = paginate(pageNumber, pageSize, "select * ", "from gc_schedule_plan order by pid asc");
    return pagePlan;
  }
  
  public List<Plan> getEntityList(QueryFilter queryFilter)
  {
    return (List<Plan>) this.dataService.getEntityList("gc_schedule_plan", queryFilter, me);
  }
}

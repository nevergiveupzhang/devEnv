package cn.cnnic.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

import cn.cnnic.common.DataService;
import cn.cnnic.common.QueryFilter;

import java.util.List;

public class PlanOrder
  extends Model<PlanOrder>
{
  public static final PlanOrder me = new PlanOrder();
  public static final String tableName = "gc_schedule_planorder";
  DataService dataService = new DataService();
  
  public Page<PlanOrder> paginate(int pageNumber, int pageSize)
  {
    Page<PlanOrder> pagePlanOrder = paginate(pageNumber, pageSize, 
      "select * ", "from gc_schedule_planorder order by id desc");
    return pagePlanOrder;
  }
  
  public List<PlanOrder> getEntityList(QueryFilter queryFilter)
  {
    return (List<PlanOrder>) this.dataService.getEntityList("gc_schedule_planorder", 
      queryFilter, me);
  }
}

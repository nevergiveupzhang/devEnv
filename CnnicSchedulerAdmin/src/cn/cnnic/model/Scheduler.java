package cn.cnnic.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

import cn.cnnic.common.DataService;
import cn.cnnic.common.QueryFilter;

import java.util.List;

public class Scheduler
  extends Model<Scheduler>
{
  public static final Scheduler me = new Scheduler();
  public static final String tableName = "gc_schedule_scheduler";
  DataService dataService = new DataService();
  
  public Page<Scheduler> paginate(int pageNumber, int pageSize)
  {
    Page<Scheduler> page = paginate(pageNumber, pageSize, "select * ", "from gc_schedule_scheduler order by pid desc");
    return page;
  }
  
  public List<Scheduler> getEntityList(QueryFilter queryFilter)
  {
    return (List<Scheduler>) this.dataService.getEntityList("gc_schedule_scheduler", queryFilter, me);
  }
}

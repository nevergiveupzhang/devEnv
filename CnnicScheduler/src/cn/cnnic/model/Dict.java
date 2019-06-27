package cn.cnnic.model;

import cn.cnnic.common.DataService;
import cn.cnnic.common.QueryFilter;
import com.jfinal.plugin.activerecord.Model;
import java.util.List;

public class Dict
  extends Model<Dict>
{
  public static Dict me = new Dict();
  DataService dataService = new DataService();
  
  public List<Dict> find(QueryFilter queryFilter)
  {
    return (List<Dict>) this.dataService.getEntityList("gc_common_dict", queryFilter, me);
  }
}

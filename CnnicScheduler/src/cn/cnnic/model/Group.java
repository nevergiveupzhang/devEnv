package cn.cnnic.model;

import cn.cnnic.common.DataService;
import cn.cnnic.common.QueryFilter;
import cn.cnnic.common.UpdateFilter;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import java.util.List;

public class Group
  extends Model<Group>
{
  public static Group me = new Group();
  DataService dataService = new DataService();
  
  public List<Group> find(QueryFilter queryFilter)
  {
    return (List<Group>) this.dataService.getEntityList("gc_schedule_group", queryFilter, me);
  }
  
  public int deleteAll()
  {
    return Db.update("delete from gc_schedule_group where 1=1");
  }
  
  public int update(UpdateFilter updateFiter)
  {
    String sql = "update gc_schedule_group " + updateFiter.getSetFields() + " where " + updateFiter.getWhereString();
    return Db.update(sql);
  }
}

package cn.cnnic.model;

import cn.cnnic.common.DataService;
import cn.cnnic.common.QueryFilter;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import java.util.List;

public class GroupPerson
  extends Model<GroupPerson>
{
  public static final GroupPerson me = new GroupPerson();
  DataService dataService = new DataService();
  
  public Page<GroupPerson> paginate(int pageNumber, int pageSize)
  {
    Page<GroupPerson> page = paginate(pageNumber, pageSize, "select * ", "from gc_schedule_group_person_v order by pid desc");
    return page;
  }
  
  public List<GroupPerson> getEntityList(QueryFilter queryFilter)
  {
    return (List<GroupPerson>) this.dataService.getEntityList("gc_schedule_group_person_v", queryFilter, me);
  }
}

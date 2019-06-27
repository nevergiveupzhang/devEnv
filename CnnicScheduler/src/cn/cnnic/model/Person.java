package cn.cnnic.model;

import cn.cnnic.common.DataService;
import cn.cnnic.common.QueryFilter;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import java.util.List;

public class Person
  extends Model<Person>
{
  public static final Person me = new Person();
  DataService dataService = new DataService();
  
  public Page<Person> paginate(int pageNumber, int pageSize)
  {
    Page<Person> pagePerson = paginate(pageNumber, pageSize, "select * ", "from gc_schedule_person order by pid desc");
    return pagePerson;
  }
  
  public List<Person> find(QueryFilter queryFilter)
  {
    return (List<Person>) this.dataService.getEntityList("gc_schedule_person", queryFilter, me);
  }
}

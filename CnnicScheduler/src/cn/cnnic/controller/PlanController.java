package cn.cnnic.controller;

import cn.cnnic.common.DataService;
import cn.cnnic.common.QueryFilter;
import cn.cnnic.model.Plan;
import cn.cnnic.utils.DataUtils;
import com.jfinal.core.Controller;
import java.util.List;

public class PlanController
  extends Controller
{
  public DataService dataService = null;
  
  public PlanController()
  {
    this.dataService = new DataService();
  }
  
  public void index() {}
  
  public void getList()
  {
    QueryFilter queryFilter = new QueryFilter();
    if (getPara() == null) {
      queryFilter.setWhereString("1=1");
    } else {
      queryFilter.setWhereString(getPara());
    }
    queryFilter.setSelectFields("*");
    queryFilter.setOrderString("pid asc");
    List<Plan> dictList = Plan.me.getEntityList(queryFilter);
    String result = DataUtils.listToJsonStr(dictList, Plan.me);
    renderJson(result);
  }
}

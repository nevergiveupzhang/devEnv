package cn.cnnic.controller;

import cn.cnnic.common.DataService;
import cn.cnnic.common.QueryFilter;
import cn.cnnic.model.Plan;
import cn.cnnic.model.PlanOrder;
import cn.cnnic.utils.DataUtils;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Page;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlanController
  extends Controller
{
  private static final Logger LOGGER = LoggerFactory.getLogger(PlanController.class);
  public DataService dataService = null;
  
  public PlanController()
  {
    this.dataService = new DataService();
  }
  
  public void index()
  {
    HttpServletRequest request = getRequest();
    HttpServletResponse response = getResponse();
    renderPlanIndex(request, response, 6);
    renderNull();
  }
  
  public void renderPlanIndex(HttpServletRequest request, HttpServletResponse response, int pageSize)
  {
    int pageNumber = 1;
    
    String pno = request.getParameter("pno");
    if (pno != null) {
      pageNumber = Integer.parseInt(pno);
    }
    if (pageSize == 0) {
      pageSize = 10;
    }
    Page<Plan> page = Plan.me.paginate(pageNumber, pageSize);
    GeneralController.renderIndex(request, response, page);
  }
  
  public void edit()
  {
    LOGGER.info("--------------------[PlanController->edit]\t\trender edit page");
    Map<String, Serializable> root = new HashMap();
    HttpServletRequest request = getRequest();
    HttpServletResponse response = getResponse();
    
    String pid = request.getParameter("pid");
    LOGGER.info("--------------------[PlanController->edit]\t\tplan id is [" + pid + "]");
    root.put("result", Plan.me.findById(pid));
    GeneralController.renderTemplate(request, response, root);
    renderNull();
  }
  
  @Before({POST.class})
  public void update()
  {
    String updatedJson = getPara("updated");
    LOGGER.info("--------------------[PlanController->update]\t\tbegin to update plan");
    LOGGER.info("--------------------[PlanController->update]\t\tupdatedJson: " + updatedJson);
    boolean result = this.dataService.update(updatedJson, Plan.class);
    LOGGER.info("--------------------[PlanController->update]\t\tupdating plan finished");
    renderJson(Boolean.valueOf(result));
  }
  
  @Before({POST.class})
  public void delete()
  {
    Integer pid = getParaToInt(0);
    LOGGER.info("--------------------[PlanController->delete]\t\tbegin to delete plan");
    LOGGER.info("--------------------[PlanController->delete]\t\tplan id is [" + pid + "]");
    boolean result = Plan.me.deleteById(pid);
    LOGGER.info("--------------------[PlanController->delete]\t\tdeleting plan finished");
    renderJson(Boolean.valueOf(result));
  }
  
  public void add()
  {
    LOGGER.info("--------------------[PlanController->add]\t\trender add page");
    Map<String, Serializable> root = new HashMap();
    HttpServletRequest request = getRequest();
    HttpServletResponse response = getResponse();
    GeneralController.renderTemplate(request, response, root);
    renderNull();
  }
  
  @Before({POST.class})
  public void save()
  {
    LOGGER.info("--------------------[PlanController->save]\t\tbegin to save plan");
    String insertedJson = getPara("inserted");
    LOGGER.info("--------------------[PlanController->save]\t\tinsertedJson:" + insertedJson);
    boolean result = this.dataService.save(insertedJson, Plan.class);
    LOGGER.info("--------------------[PlanController->save]\t\tsaving plan finished");
    renderJson(Boolean.valueOf(result));
  }
  
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
    LOGGER.info("--------------------[PlanController->getList]\t\t" + queryFilter.toString());
    List<Plan> dictList = Plan.me.getEntityList(queryFilter);
    String result = DataUtils.listToJsonStr(dictList, Plan.me);
    renderJson(result);
  }
  
  public void getPlanOrderList()
  {
    QueryFilter queryFilter = new QueryFilter();
    if (getPara() == null) {
      queryFilter.setWhereString("1=1");
    } else {
      queryFilter.setWhereString(getPara());
    }
    queryFilter.setSelectFields("*");
    queryFilter.setOrderString("id desc");
    LOGGER.info("--------------------[PlanController->getPlanOrderList]\t\t" + queryFilter.toString());
    List<PlanOrder> dictList = PlanOrder.me.getEntityList(queryFilter);
    String result = DataUtils.listToJsonStr(dictList, PlanOrder.me);
    renderJson(result);
  }
  
  @Before({POST.class})
  public void savePlanOrder()
  {
    LOGGER.info("--------------------[PlanController->savePlanOrder]\t\tbegin to save PlanOrder");
    String insertedJson = getPara("inserted");
    LOGGER.info("--------------------[PlanController->savePlanOrder]\t\tinsertedJson:" + insertedJson);
    boolean result = this.dataService.save(insertedJson, PlanOrder.class);
    LOGGER.info("--------------------[PlanController->savePlanOrder]\t\tsaving PlanOrder finished");
    renderJson(Boolean.valueOf(result));
  }
  
  @Before({POST.class})
  public void updatePlanOrder()
  {
    LOGGER.info("--------------------[PlanController->updatePlanOrder]\t\tbegin to update PlanOrder");
    String updatedJson = getPara("updated");
    LOGGER.info("--------------------[PlanController->updatePlanOrder]\t\tupdatedJson:" + updatedJson);
    boolean result = this.dataService.update(updatedJson, PlanOrder.class);
    LOGGER.info("--------------------[PlanController->updatePlanOrder]\t\tupdating PlanOrder finished");
    renderJson(Boolean.valueOf(result));
  }
  
  @Before({POST.class})
  public void delPlanOrder()
  {
    LOGGER.info("--------------------[PlanController->delPlanOrder]\t\tbegin to delete PlanOrder");
    String whereString = getPara("whereString");
    LOGGER.info("--------------------[PlanController->delPlanOrder]\t\twhereString:" + whereString);
    int result = this.dataService.del("gc_schedule_planorder", whereString);
    LOGGER.info("--------------------[PlanController->delPlanOrder]\t\tdeleting PlanOrder finished");
    renderJson(Integer.valueOf(result));
  }
}

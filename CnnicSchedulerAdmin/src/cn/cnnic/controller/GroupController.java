package cn.cnnic.controller;

import cn.cnnic.common.DataService;
import cn.cnnic.common.QueryFilter;
import cn.cnnic.common.UpdateFilter;
import cn.cnnic.model.Group;
import cn.cnnic.model.Plan;
import cn.cnnic.utils.DataUtils;
import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroupController
  extends Controller
{
  private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);
  private DataService dataService=new DataService();
  
  public void index() {}
  
  @ActionKey("/queryGroup")
  public void queryGroup()
  {
    QueryFilter queryFilter = new QueryFilter();
    String selectFields = getPara("selectFields");
    String whereString = getPara("whereString");
    String orderString = getPara("orderString");
    if (whereString == null) {
      whereString = "1=1";
    }
    if (selectFields == null) {
      selectFields = "*";
    }
    if (orderString == null) {
      orderString = "gid asc";
    }
    queryFilter.setSelectFields(selectFields);
    queryFilter.setWhereString(whereString);
    List<Group> dictList = Group.me.find(queryFilter);
    String result = DataUtils.listToJsonStr(dictList, Group.me);
    renderJson(result);
  }
  
  @Before({POST.class})
  @ActionKey("/saveGroup")
  public void saveGroup()
  {
    boolean result = false;
    String insertedJson = getPara("inserted");
    String updatedJson = getPara("updated");
    if ((insertedJson != null) && (!insertedJson.equals("")))
    {
      LOGGER.info("--------------------[GroupController->saveGroup]\t\tinsertedJson=" + insertedJson);
      
      JSONArray jsonArray = JSONArray.fromObject(insertedJson);
      
      List<Map<String, Object>> mapListJson = jsonArray;
      
      Group group = null;
      for (int i = 0; i < mapListJson.size(); i++)
      {
        Map<String, Object> map = (Map)mapListJson.get(i);
        group = new Group();
        result = ((Group)group.setAttrs(map)).save();
      }
    }
    if ((updatedJson != null) && (!updatedJson.equals("")))
    {
      LOGGER.info("--------------------[GroupController->saveGroup]\t\tupdatedJson=" + updatedJson);
      JSONArray updatedJsonArray = JSONArray.fromObject(updatedJson);
      List<Map<String, Object>> updatedListJson = updatedJsonArray;
      Group updateGroup = null;
      for (int i = 0; i < updatedListJson.size(); i++)
      {
        Map<String, Object> map = (Map)updatedListJson.get(i);
        updateGroup = new Group();
        result = ((Group)updateGroup.setAttrs(map)).update();
      }
    }
    renderJson(Boolean.valueOf(result));
  }
  
  @ActionKey("delGroupById")
  public void deleteById()
  {
    Integer groupId = getParaToInt("groupId");
    LOGGER.info("--------------------[GroupController->deleteById]\t\tbegin to delete group,group id is [" + groupId + "]");
    boolean result = Group.me.deleteById(groupId);
    LOGGER.info("--------------------[GroupController->deleteById]\t\tdeleting group [id=" + groupId + "] finished");
    renderJson(Boolean.valueOf(result));
  }
  
  @ActionKey("delAllGroup")
  public void deleteAll()
  {
    LOGGER.info("--------------------[GroupController->deleteAll]\t\tbegin to delete all groups");
    int result = Group.me.deleteAll();
    LOGGER.info("--------------------[GroupController->deleteAll]\t\tdeleting all groups finished");
    renderJson(Integer.valueOf(result));
  }
  
  @ActionKey("updateGroup")
  public void update()
  {
    LOGGER.info("--------------------[GroupController->update]\t\tbegin to update group");
    String updatedJson = getPara("updated");
    LOGGER.info("--------------------[PlanController->update]\t\tupdatedJson: " + updatedJson);
    boolean result = this.dataService.update(updatedJson, Group.class);
    renderJson(Boolean.valueOf(result));
    LOGGER.info("--------------------[GroupController->update]\t\tupdating group finished");
  }
}

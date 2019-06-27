package cn.cnnic.common;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;

public class DataService
  implements IDataService
{
  public boolean update(String insertedJson, Class<?> c)
  {
    boolean result = false;
    if ((insertedJson != null) && (!insertedJson.equals("")))
    {
      JSONArray jsonArray = JSONArray.fromObject(insertedJson);
      
      List<Map<String, Object>> mapListJson = jsonArray;
      for (int i = 0; i < mapListJson.size(); i++)
      {
        Map<String, Object> map = (Map)mapListJson.get(i);
        try
        {
          result = ((Model)c.newInstance()).setAttrs(map).update();
        }
        catch (InstantiationException e)
        {
          e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
          e.printStackTrace();
        }
      }
    }
    return result;
  }
  
  public int update(String tableName, UpdateFilter updateFilter)
  {
    String whereString = updateFilter.getWhereString();
    String setFields = updateFilter.getSetFields();
    if (("".equals(tableName)) || (tableName == null)) {
      return 0;
    }
    if (("".equals(whereString)) || (whereString == null)) {
      whereString = "1=1";
    }
    if (("".equals(setFields)) || (setFields == null)) {
      return 0;
    }
    String sql = "update " + tableName + " set " + setFields + " where (" + whereString + ")";
    
    return Db.update(sql);
  }
  
  public boolean save(String insertedJson, Class<?> c)
  {
    boolean result = false;
    if ((insertedJson != null) && (!insertedJson.equals("")))
    {
      JSONArray jsonArray = JSONArray.fromObject(insertedJson);
      
      List<Map<String, Object>> mapListJson = jsonArray;
      for (int i = 0; i < mapListJson.size(); i++)
      {
        Map<String, Object> map = (Map)mapListJson.get(i);
        try
        {
          result = ((Model)c.newInstance()).setAttrs(map).save();
        }
        catch (InstantiationException e)
        {
          e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
          e.printStackTrace();
        }
      }
    }
    return result;
  }
  
  public List<?> getEntityList(String tableName, QueryFilter queryFilter, Model<?> model)
  {
    String whereString = queryFilter.getWhereString();
    String orderString = queryFilter.getOrderString();
    String selectFields = queryFilter.getSelectFields();
    if (("".equals(tableName)) || (tableName == null)) {
      return null;
    }
    if (("".equals(whereString)) || (whereString == null)) {
      whereString = "1=1";
    }
    if (("".equals(selectFields)) || (selectFields == null)) {
      selectFields = "*";
    }
    String sql = "select " + selectFields + " from " + tableName + " where (" + whereString + ")";
    if ((!"".equals(orderString)) && (orderString != null)) {
      sql = sql + " order by " + orderString;
    }
    return model.find(sql);
  }
  
  public int del(String tableName, String whereString)
  {
    if ((whereString == null) || ("".equals(whereString))) {
      whereString = "1=1";
    }
    String sql = "delete from " + tableName + " where (" + whereString + ")";
    return Db.update(sql);
  }
}

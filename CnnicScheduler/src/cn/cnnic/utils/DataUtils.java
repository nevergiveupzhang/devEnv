package cn.cnnic.utils;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import java.io.PrintStream;
import java.util.List;

public class DataUtils
{
  public static String pageToJson(Page<?> paginate, Model<?> model)
  {
    StringBuilder jsonBuilder = new StringBuilder();
    System.out.println(paginate);
    List<?> list = paginate.getList();
    
    int totalRow = paginate.getTotalRow();
    jsonBuilder.append("{\"total\":");
    jsonBuilder.append(totalRow);
    jsonBuilder.append(",\"rows\":[");
    for (int i = 0; i < list.size(); i++)
    {
      model = (Model)list.get(i);
      
      jsonBuilder.append(model.toJson());
      jsonBuilder.append(",");
    }
    jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
    jsonBuilder.append("]}");
    return jsonBuilder.toString();
  }
  
  public static String listToJsonStr(List<?> list, Model<?> model)
  {
    StringBuilder jsonBuilder = new StringBuilder();
    if (list.size() == 0) {
      return "[]";
    }
    jsonBuilder.append("[");
    for (int i = 0; i < list.size(); i++)
    {
      model = (Model)list.get(i);
      
      jsonBuilder.append(model.toJson());
      jsonBuilder.append(",");
    }
    jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
    jsonBuilder.append("]");
    return jsonBuilder.toString();
  }
}

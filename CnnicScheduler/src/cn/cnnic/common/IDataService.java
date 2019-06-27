package cn.cnnic.common;

import com.jfinal.plugin.activerecord.Model;
import java.util.List;

public abstract interface IDataService
{
  public abstract boolean save(String paramString, Class<?> paramClass);
  
  public abstract boolean update(String paramString, Class<?> paramClass);
  
  public abstract int update(String paramString, UpdateFilter paramUpdateFilter);
  
  public abstract List<?> getEntityList(String paramString, QueryFilter paramQueryFilter, Model<?> paramModel);
  
  public abstract int del(String paramString1, String paramString2);
}

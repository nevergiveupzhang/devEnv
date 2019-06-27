package cn.cnnic.config;

import cn.cnnic.model.Dict;
import cn.cnnic.model.Group;
import cn.cnnic.model.GroupPerson;
import cn.cnnic.model.Person;
import cn.cnnic.model.Plan;
import cn.cnnic.model.PlanOrder;
import cn.cnnic.model.ScheduleLog;
import cn.cnnic.model.Scheduler;
import cn.cnnic.model.SecondLine;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.UrlSkipHandler;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;

public class ScheduleConfig
  extends JFinalConfig
{
  public void configConstant(Constants me)
  {
    PropKit.use("jdbc.properties");
    
    me.setDevMode(PropKit.getBoolean("devMode", Boolean.valueOf(false)).booleanValue());
    me.setBaseViewPath("/WEB-INF/views/");
    me.setError401View("/WEB-INF/views/error/401.html");
    me.setError403View("/WEB-INF/views/error/403.html");
    me.setError404View("/WEB-INF/views/error/404.html");
    me.setError500View("/WEB-INF/views/error/500.html");
  }
  
  public void configRoute(Routes me)
  {
	  me.add(new FrontRoutes());
    
  }
  
  public void configPlugin(Plugins me)
  {
    C3p0Plugin c3p0Plugin = new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
    me.add(c3p0Plugin);
    
    ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
    arp.setShowSql(true);
    me.add(arp);
    
    arp.addMapping("gc_schedule_person", "pid", Person.class);
    
    arp.addMapping("gc_common_dict", Dict.class);
    
    arp.addMapping("gc_schedule_group", "gid", Group.class);
    
    arp.addMapping("gc_schedule_plan", "pid", Plan.class);
    arp.addMapping("gc_schedule_planorder", PlanOrder.class);
    
    arp.addMapping("gc_schedule_group_person_v", "gid", GroupPerson.class);
    arp.addMapping("gc_schedule_scheduler", Scheduler.class);
    arp.addMapping("gc_schedule_secondline","sid",SecondLine.class);
  	 arp.addMapping("gc_schedule_log", ScheduleLog.class);
  }
  
  public void configInterceptor(Interceptors me) {}
  
  public void configHandler(Handlers me)
  {
    me.add(new UrlSkipHandler("/home", false));
  }
  
  public static void main(String[] args)
  {
    JFinal.start("WebRoot", 8000, "/", 5);
  }

}

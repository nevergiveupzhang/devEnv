package cn.cnnic.config;

import cn.cnnic.controller.IndexController;
import cn.cnnic.controller.PersonController;
import cn.cnnic.controller.PlanController;
import cn.cnnic.controller.ScheduleLogController;
import cn.cnnic.controller.SchedulerController;
import cn.cnnic.controller.SecondLineController;

import com.jfinal.config.Routes;

public class FrontRoutes
  extends Routes
{
  public void config()
  {
    add("/", IndexController.class);
    add("/plan", PlanController.class, "/planset");
    add("/schedule", SchedulerController.class);
    add("/secondline", SecondLineController.class);
    add("/person",PersonController.class);
    add("/history",ScheduleLogController.class);
  }
}

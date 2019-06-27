package cn.cnnic.config;

import cn.cnnic.controller.DictController;
import cn.cnnic.controller.GroupController;
import cn.cnnic.controller.IndexController;
import cn.cnnic.controller.PersonController;
import cn.cnnic.controller.PlanController;
import cn.cnnic.controller.ScheduleLogController;
import cn.cnnic.controller.SchedulerController;
import cn.cnnic.controller.SecondLineController;
import cn.cnnic.controller.UploadController;

import com.jfinal.config.Routes;

public class FrontRoutes extends Routes
{
  public void config()
  {
    add("/", IndexController.class);
    add("/person", PersonController.class);
    add("/dict", DictController.class);
    add("/group", GroupController.class);
    add("/plan", PlanController.class);
    add("/schedule", SchedulerController.class);
    add("/upload", UploadController.class);
    add("/secondline", SecondLineController.class);
    add("/history",ScheduleLogController.class);
  }
}

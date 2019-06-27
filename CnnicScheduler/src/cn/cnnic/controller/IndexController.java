package cn.cnnic.controller;

import com.jfinal.core.Controller;

public class IndexController
  extends Controller
{
  public void index()
  {
    render("index.html");
    setAttr("finalshceduler", "运行管理部排版表");
  }
}

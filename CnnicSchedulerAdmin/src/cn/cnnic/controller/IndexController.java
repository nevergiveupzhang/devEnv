package cn.cnnic.controller;

import com.jfinal.core.Controller;

/**
 * IndexController
 */
public class IndexController extends Controller {
	public void index() {
		render("index.html");
		setAttr("finalshceduler", "运行管理部排班系统");
	}
	
	public void stack() {
		getResponse().setCharacterEncoding("UTF-8");
		renderJsp("stack.jsp");
	}
}






package cn.cnnic.plan.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cnnic.plan.exception.ErrPageException;

@Controller
@RequestMapping("**")
public class ErrorController {
	
	@RequestMapping("**")
	@ResponseBody
	public String errPage(HttpServletRequest request) {
		throw new ErrPageException("request ["+request.getMethod()+" "+request.getRequestURI()+ "]  is not found!");
	}
}

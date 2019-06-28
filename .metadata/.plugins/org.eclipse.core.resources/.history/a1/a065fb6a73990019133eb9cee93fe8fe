package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.service.UserService;

@Controller
@RequestMapping("/")
public class UserController {
	@Autowired
	private UserService userService;
	
	@RequestMapping("login")
	@ResponseBody
	public String login(@RequestParam String userName,@RequestParam String password) {
		userService.login(userName,password);
		return "success";
	}
	
}

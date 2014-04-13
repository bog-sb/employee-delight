package com.up.employeedelight.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import thymeleaf.spring.support.Layout;

/**
 * Handles requests for the pages involving user authentication. 
 */
@Layout(value="layouts/main_layout.html")
@Controller
@RequestMapping
public class AuthenticationController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)	
	public String getLoginPage(){
		return "/user/login_form.html";
	}
	
	@RequestMapping(value = "/logout")	
	public String logout(){
		return "/user/login_form.html";
	}
}

package com.up.employeedelight.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import thymeleaf.spring.support.Layout;

import com.google.gson.Gson;
import com.up.employeedelight.domain.User;
import com.up.employeedelight.exceptions.EDIllegalOperation;
import com.up.employeedelight.service.UserManager;

/**
 * Handles requests for the application home page.
 */
@Layout(value="layouts/main_layout.html")
@Controller
@ComponentScan(value="com.urban.pooling.service")
public class UserController {
	Gson gson = new Gson();
	
	@Autowired
	UserManager userManager;

	private static final Logger logger = LoggerFactory
			.getLogger(UserController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("User Controller", locale);

		return "admin/users.html";
	}
	
	@RequestMapping(value="users/add", method = RequestMethod.POST,consumes="application/json",headers = "content-type=application/x-www-form-urlencoded")
	public @ResponseBody String addUser( @RequestBody String data){
		try {		
			logger.info("User Controller:: add - " + data);
			System.out.println("----- User Controller:: add - " + data);
			User user = gson.fromJson(data, User.class);
			System.out.println("--- user " + user.toString());
			userManager.addUser(user);
			return gson.toJson("User has been successfully added!");
		} catch (EDIllegalOperation e) {
			e.printStackTrace();
			logger.debug("",e);
			return gson.toJson(e.getMessage());
		}
	}
	

	@RequestMapping(value="users/remove", method = RequestMethod.POST,consumes="application/json",headers = "content-type=application/x-www-form-urlencoded")
	public @ResponseBody String removeUser( @RequestParam int id){
		try {		
			logger.info("User Controller:: remove - " + id);
			System.out.println("----- User Controller:: remove - " + id);
		
			userManager.removeUser(id);
			return gson.toJson("User has been successfully removed!");
		} catch (EDIllegalOperation e) {
			e.printStackTrace();
			logger.debug("",e);
			return gson.toJson(e.getMessage());
		}
	}
	
	@RequestMapping(value="users/update", method = RequestMethod.POST,consumes="application/json",headers = "content-type=application/x-www-form-urlencoded")
	public @ResponseBody String updateUser( @RequestBody String data){
			logger.info("User Controller:: update - " + data);
			System.out.println("----- User Controller:: add - " + data);
			User user = gson.fromJson(data, User.class);
			System.out.println("--- user " + user.toString());
			userManager.updateUser(user);
			return gson.toJson("User has been successfully added!");
	}
	
	@RequestMapping(value="users/get", method = RequestMethod.POST)
	public @ResponseBody String getUsers(){
		return gson.toJson(userManager.getUsers());
	}

}

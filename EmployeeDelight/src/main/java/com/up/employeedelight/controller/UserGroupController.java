package com.up.employeedelight.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import thymeleaf.spring.support.Layout;

import com.up.employeedelight.service.UserGroupManager;

/**
 * Handles requests for the application home page.
 */
@Layout(value = "layouts/main_layout.html")
@Controller
@RequestMapping(value = "/groups")
@ComponentScan(value = "com.urban.pooling.service")
public class UserGroupController {
	@Autowired
	private UserGroupManager ugManager;

	private static final Logger logger = LoggerFactory.getLogger(UserGroupController.class);

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String showUpdateGroupPoints() {
		return "updateGroupPoints.html";
	}

	@RequestMapping(value = "/updatePoints", method = RequestMethod.POST)
	public String updatePoints(@RequestParam Integer points, @RequestParam String groupName) {
		ugManager.updateGroupPoints(groupName, points);
		return "updateGroupPoints.html";
	}
}

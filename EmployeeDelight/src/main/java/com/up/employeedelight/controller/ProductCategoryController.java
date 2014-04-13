package com.up.employeedelight.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import thymeleaf.spring.support.Layout;

/**
 * Handles requests for the application home page.
 */
@Layout(value="layouts/sidebar_layout.html")
@Controller
@ComponentScan(value="com.urban.pooling.service")
public class ProductCategoryController {

	private static final Logger logger = LoggerFactory
			.getLogger(ProductCategoryController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/category", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Login Controller", locale);

		return "test-content.html";
	}

}

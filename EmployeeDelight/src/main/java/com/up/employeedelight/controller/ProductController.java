package com.up.employeedelight.controller;

import java.util.Locale;

import org.junit.Ignore;
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

import com.google.gson.Gson;
import com.up.employeedelight.domain.Product;
import com.up.employeedelight.exceptions.EDIllegalOperation;
import com.up.employeedelight.service.ProductManager;

import thymeleaf.spring.support.Layout;

/**
 * Handles requests for the application home page.
 */
@Layout(value="layouts/main_layout.html")
@Controller
@ComponentScan(value="com.urban.pooling.service")
public class ProductController {
	Gson gson = new Gson();
	
	@Autowired
	ProductManager prodMan;

	private static final Logger logger = LoggerFactory
			.getLogger(ProductController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Product Controller", locale);

		return "admin/products.html";
	}
	
	@RequestMapping(value="products/add", method = RequestMethod.POST,consumes="application/json",headers = "content-type=application/x-www-form-urlencoded")
	public @ResponseBody String addProduct( @RequestBody String data){
		try {		
			logger.info("Product Controller:: add - " + data);
			System.out.println("----- Product Controller:: add - " + data);
			Product prod = gson.fromJson(data, Product.class);
			System.out.println("--- product " + prod.toString());
			prodMan.addProduct(prod);
			return gson.toJson("Product has been successfully added!");
		} catch (EDIllegalOperation e) {
			e.printStackTrace();
			logger.debug("",e);
			return gson.toJson(e.getMessage());
		}
	}
	

	@RequestMapping(value="products/remove", method = RequestMethod.POST,consumes="application/json",headers = "content-type=application/x-www-form-urlencoded")
	public @ResponseBody String removeProduct( @RequestParam int id){
		try {		
			logger.info("Product Controller:: remove - " + id);
			System.out.println("----- Product Controller:: remove - " + id);
		
			prodMan.removeProd(id);
			return gson.toJson("Product has been successfully removed!");
		} catch (EDIllegalOperation e) {
			e.printStackTrace();
			logger.debug("",e);
			return gson.toJson(e.getMessage());
		}
	}
	
	@RequestMapping(value="products/update", method = RequestMethod.POST,consumes="application/json",headers = "content-type=application/x-www-form-urlencoded")
	public @ResponseBody String updateProduct( @RequestBody String data){
			logger.info("Product Controller:: update - " + data);
			System.out.println("----- Product Controller:: add - " + data);
			Product prod = gson.fromJson(data, Product.class);
			System.out.println("--- product " + prod.toString());
			prodMan.updateProd(prod);
			return gson.toJson("Product has been successfully added!");
	}
	
	@RequestMapping(value="products/get", method = RequestMethod.POST)
	public @ResponseBody String getProducts(){
		return gson.toJson(prodMan.getProductsAsList());
	}

}

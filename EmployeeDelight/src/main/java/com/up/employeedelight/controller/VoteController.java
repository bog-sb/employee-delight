package com.up.employeedelight.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import thymeleaf.spring.support.Layout;

import com.up.employeedelight.domain.User;
import com.up.employeedelight.repository.UserGroupRepository;
import com.up.employeedelight.service.ProductCategoryManager;
import com.up.employeedelight.service.ProductManager;
import com.up.employeedelight.service.UserManager;
import com.up.employeedelight.service.VoteManager;

/**
 * Handles requests for the application home page.
 */
@Layout(value = "layouts/main_layout.html")
@Controller
@ComponentScan(value = "com.urban.pooling.service")
public class VoteController {

	private static final String FIRST_LOGIN = "firstLogin";

	private static final Logger logger = LoggerFactory.getLogger(VoteController.class);

	@Autowired
	private ProductCategoryManager productCategoryManager;

	@Autowired
	private UserGroupRepository userGroupRepo;

	@Autowired
	private ProductManager productManager;

	@Autowired
	private VoteManager voteManager;

	@Autowired
	private UserManager userManager;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(HttpServletRequest request, Model model, @Value("${welcome.message.title}") String welcomeTitle,
			@Value("${welcome.message.body}") String welcomeBody) {

		// Shows the welcome message only once on the current session
		if (request.getSession().getAttribute(FIRST_LOGIN) == null) {
			model.addAttribute("welcomeTitle", welcomeTitle);
			model.addAttribute("welcomeBody", welcomeBody);
			request.getSession().setAttribute(FIRST_LOGIN, "false");
		}

		User user = userManager.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		model.addAttribute("points", user.getPoints());

		// add categories
		model.addAttribute("productCategories", productCategoryManager.getAll());
		model.addAttribute("productLists", productManager.getProducts());

		return "vote-content.html";
	}

	@RequestMapping(value = "/vote/submit", method = RequestMethod.POST)
	public @ResponseBody
	Integer submitVote(@RequestParam Integer id, @RequestParam Integer points) {
		return voteManager.submitVote(id, points);
	}
}

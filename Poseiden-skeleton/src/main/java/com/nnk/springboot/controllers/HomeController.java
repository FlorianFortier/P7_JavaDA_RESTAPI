package com.nnk.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller class for handling home-related requests.
 */
@Controller
public class HomeController {

	/**
	 * Handler for requests to the home page ("/").
	 *
	 * @param model The model to be populated with data.
	 * @return The view name for the home page.
	 */
	@RequestMapping("/")
	public String home(Model model) {
		return "home";
	}

	/**
	 * Handler for requests to the admin home page ("/admin/home").
	 * Redirects to the bid list page.
	 *
	 * @param model The model to be populated with data.
	 * @return The view name for redirection to the bid list page.
	 */
	@RequestMapping("/admin/home")
	public String adminHome(Model model) {
		return "redirect:/bidList/list";
	}
}

package com.blogapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.blogapp.entities.User;
import com.blogapp.services.UserServices;

@Controller
public class UserController {
	@Autowired
	UserServices userService;

	@PostMapping("/register")
	public String registerUser(@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("password") String password) {
		boolean isSaved = userService.registerUser(name, email, password);
		if (isSaved) {
			return "redirect:/login";
		}
		return "redirect:/register";
	}

	@PostMapping("/login")
	public String userLogin(Model model, @RequestParam("email") String email,
			@RequestParam("password") String password) {
		User user = userService.AuthenticateUser(email, password);
		if (user != null) {
			return "redirect:/account";
		}
		return "redirect:/login";
	}

}

package com.blogapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.blogapp.entities.User;
import com.blogapp.services.UserServices;

@Controller
public class UserController {
	@Autowired
	UserServices userService;
	
	@RequestMapping(value = "/register",method = RequestMethod.POST)
	public String registerUser(@RequestParam("name") String name,@RequestParam("email") String email,@RequestParam("password") String password){
		boolean isSaved = userService.registerUser(name, email, password);
		if(isSaved) {
			return "redirect:/login";
		}
		return "redirect:/register";
	}
	
	@RequestMapping(value = "/login",method = RequestMethod.POST)
	public String userLogin(Model model, @RequestParam("email") String email,@RequestParam("password") String password) {
		User user = userService.AuthenticateUser(email, password);
		if(user != null) {
			return "redirect:/account";
		}
		return "redirect:/login";
	}
	
	
}

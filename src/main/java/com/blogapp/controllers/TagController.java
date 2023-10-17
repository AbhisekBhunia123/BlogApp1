package com.blogapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.blogapp.services.TagServices;

@Controller
public class TagController {
	@Autowired
	TagServices tagServices;
	
	@RequestMapping(value = "/createtag",method = RequestMethod.POST)
	public String createTag(@RequestParam("name") String name,@RequestParam("postId") int postId,@RequestParam("userId") int userId) {
		tagServices.createTag(name,postId);
		System.out.println(postId);
		return "redirect:/publishedpost/"+userId;
	}

}

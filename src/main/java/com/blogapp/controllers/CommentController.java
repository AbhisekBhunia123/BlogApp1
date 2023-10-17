package com.blogapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.blogapp.services.CommentService;

@Controller
public class CommentController {
	@Autowired
	CommentService commentService;
	
	@RequestMapping(value = "/comment",method = RequestMethod.POST)
	public String makeComment(@RequestParam("comment") String comment,@RequestParam("postId") int postId) {
		commentService.makeComment(comment,postId);
		return "redirect:/account";
	}
}

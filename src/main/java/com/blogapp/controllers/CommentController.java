package com.blogapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.blogapp.services.CommentService;

@Controller
public class CommentController {
	@Autowired
	CommentService commentService;

	@PostMapping("/account/comment")
	public String makeComment(@RequestParam("comment") String comment, @RequestParam("postId") int postId,
			Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String email = userDetails.getUsername();
		commentService.makeComment(comment, postId, email);
		return "redirect:/blog/" + postId;
	}

	@PostMapping("/account/updatecomment")
	public String updateComment(@RequestParam("comment") String comment, @RequestParam("commentId") int commentId,
			@RequestParam("postId") int postId) {
		commentService.updateComment(comment, commentId);
		return "redirect:/blog/" + postId;
	}

	@PostMapping("/account/deletecomment")
	public String deleteComment(@RequestParam("commentId") int commentId, @RequestParam("postId") int postId) {
		commentService.deleteComment(commentId);
		return "redirect:/blog/" + postId;
	}
}

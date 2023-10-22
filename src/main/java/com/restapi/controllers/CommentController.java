package com.restapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blogapp.services.CommentService;

@RestController
public class CommentController {
	@Autowired
	CommentService commentService;

	@PostMapping("/account/comment")
	public ResponseEntity<String> makeComment(@RequestParam("comment") String comment, @RequestParam("postId") int postId,
			Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String email = userDetails.getUsername();
		commentService.makeComment(comment, postId, email);
		return ResponseEntity.ok("Comment added successfully");

	}

	@PostMapping("/account/updatecomment")
	public ResponseEntity<String> updateComment(@RequestParam("comment") String comment, @RequestParam("commentId") int commentId,
			@RequestParam("postId") int postId) {
		commentService.updateComment(comment, commentId);
		return ResponseEntity.ok("Comment updated successfully");
	}

	@PostMapping("/account/deletecomment")
	public ResponseEntity<String> deleteComment(@RequestParam("commentId") int commentId, @RequestParam("postId") int postId) {
		commentService.deleteComment(commentId);
		return ResponseEntity.ok("Comment Deleted successfully");
	}
}

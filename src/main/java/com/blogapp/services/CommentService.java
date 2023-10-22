package com.blogapp.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blogapp.entities.Comment;
import com.blogapp.entities.Post;
import com.blogapp.entities.User;
import com.blogapp.repositories.CommentRepo;
import com.blogapp.repositories.PostRepo;
import com.blogapp.repositories.UserRepo;

@Service
public class CommentService {
	@Autowired
	CommentRepo commentRepo;

	@Autowired
	PostRepo postRepo;

	@Autowired
	UserRepo userRepo;

	public boolean makeComment(String comment, int postId, String email) {
		boolean isCommented = false;
		try {
			User user = userRepo.findByEmail(email);
			Optional<Post> postOp = postRepo.findById(postId);
			Post post = postOp.get();
			Comment c = new Comment();
			c.setName(user.getName());
			c.setEmail(user.getEmail());
			c.setComment(comment);
			c.setPost(post);
			c.setCreatedAt(new Date());
			c.setUpdatedAt(new Date());
			commentRepo.save(c);
			isCommented = true;
		} catch (Exception e) {

		}
		return isCommented;
	}

	public boolean updateComment(String comment, int commentId) {
		boolean isUpdated = false;
		try {

			Comment com = commentRepo.findById(commentId).get();
			com.setComment(comment);
			com.setUpdatedAt(new Date());
			commentRepo.save(com);
			isUpdated = true;
		} catch (Exception e) {

		}
		return isUpdated;
	}

	public boolean deleteComment(int commentId) {
		boolean isDeleted = false;
		try {
			commentRepo.deleteById(commentId);
			isDeleted = true;
		} catch (Exception e) {

		}
		return isDeleted;
	}

}

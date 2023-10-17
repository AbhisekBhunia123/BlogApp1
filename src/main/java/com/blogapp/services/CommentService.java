package com.blogapp.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.blogapp.entities.Comment;
import com.blogapp.entities.Post;
import com.blogapp.repositories.CommentRepo;
import com.blogapp.repositories.PostRepo;

@Service
public class CommentService {
	@Autowired
	CommentRepo commentRepo;
	@Autowired
	PostRepo postRepo;
	
	public boolean makeComment(String comment,int postId) {
		boolean isCommented = false;
		try {
			Optional<Post> postOp = postRepo.findById(postId);
			Post post = postOp.get();
			Comment c = new Comment();
			c.setName("Abhi");
			c.setEmail("john@gmail.com");
			c.setComment(comment);
			c.setPost(post);
			c.setCreatedAt("date");
			c.setUpdatedAt("date");
			commentRepo.save(c);
			isCommented = true;
		}catch(Exception e) {
			
		}
		return isCommented;
	}

}

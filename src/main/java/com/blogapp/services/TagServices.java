package com.blogapp.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blogapp.entities.Post;
import com.blogapp.entities.Tag;
import com.blogapp.repositories.PostRepo;
import com.blogapp.repositories.TagRepo;

@Component
public class TagServices {

	@Autowired
	TagRepo tagRepo;
	
	@Autowired
	PostRepo postRepo;
	
	
	
	public boolean createTag(String name,int postId) {
		boolean isCreated = false;
		String createdAt = LocalDateTime.now().toString();
		String updatedAt = LocalDateTime.now().toString();
		try {
			
			Tag tag = new Tag();
			tag.setName(name);
			tag.setCreatedAt(createdAt);
			tag.setUpdatedAt(updatedAt);
			Optional<Post> posts = postRepo.findById(postId);
			Post post = posts.get();
			List<Post> list =new ArrayList<>();
			list.add(post);
			tag.setPosts(list);
			tagRepo.save(tag);
			Tag tag1 = tagRepo.findByName(name).get(0);
			post.getTags().add(tag1);
			postRepo.save(post);
			isCreated = true;
		}catch(Exception e) {
			
		}
		return isCreated;
		
	}
}

package com.blogapp.services;




import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.blogapp.entities.Post;
import com.blogapp.entities.Tag;
import com.blogapp.entities.User;
import com.blogapp.repositories.PostRepo;
import com.blogapp.repositories.TagRepo;

@Component
public class PostServices {

	@Autowired
	PostRepo postRepo;
	
	@Autowired
	TagRepo tagRepo;
	
	public boolean createPost(String title,String content,String excerpt) {
		boolean isCreated = false;
		try {
			String createdAt = LocalDateTime.now().toString();
			String updatedAt = LocalDateTime.now().toString();
			Post post = new Post();
			post.setAuthor("Abhisek");
			post.setContent(content);
			post.setTitle(title);
			post.setExcerpt(excerpt);
			post.setIsPublished("no");
			post.setPublishedAt("none");
			post.setCreatedAt(createdAt);
			post.setUpdatedAt(updatedAt);
			
			User user = new User();
			user.setId(252);
			user.setEmail("Abhi@gmail.com");
			user.setName("Abhisek Bhunia");
			user.setPassword("12345");
			post.setUser(user);
			postRepo.save(post);
			isCreated = true;
		}catch(Exception e) {
			
		}
		return isCreated;
		
	}
	
	public boolean publishPost(String title,String content,String excerpt) {
		boolean isCreated = false;
		try {
			String createdAt = LocalDateTime.now().toString();
			String updatedAt = LocalDateTime.now().toString();
			String publishedAt = LocalDateTime.now().toString();
			Post post = new Post();
			post.setAuthor("Arindam");
			post.setContent(content);
			post.setTitle(title);
			post.setExcerpt(excerpt);
			post.setIsPublished("yes");
			post.setPublishedAt(publishedAt);
			post.setCreatedAt(createdAt);
			post.setUpdatedAt(updatedAt);
			
			User user = new User();
			user.setId(252);
			user.setEmail("Abhi@gmail.com");
			user.setName("Abhisek Bhunia");
			user.setPassword("12345");
			post.setUser(user);
			postRepo.save(post);
			isCreated = true;
		}catch(Exception e) {
			
		}
		return isCreated;
		
	}
	
	public boolean publishPost(int postId) {
		boolean isPublished = false;
		try {
			Post post = postRepo.findById(postId).get();
			post.setIsPublished("yes");
			postRepo.save(post);
			isPublished = true;
			
		}catch(Exception e) {
			
		}
		return isPublished;
	}
	
	public Post getPostById(int id) {
		Post post = postRepo.findById(id).get();
		return post;	
	}
	
	public List<Post> getAllPost(){
		List<Post> posts = new ArrayList<>();
		posts = postRepo.findAll();
		return posts;
		
	}
	
	public List<Post> getDraftPost(){
		List<Post> draftPosts = new ArrayList<>();
		draftPosts = postRepo.findByIsPublished("no");
		return draftPosts;
		
	}
	
	public List<Post> getPublishedPost(){
		List<Post> publishedPost = new ArrayList<>();
		publishedPost = postRepo.findByIsPublished("yes");
		return publishedPost;
		
	}
	
	public List<Post> getPublishedPost(int userId){
		List<Post> publishedPost = postRepo.findByIsPublishedAndUserId("yes",userId);
		return publishedPost;
	}
	
	public List<Post> getDraftedPost(int userId){
		List<Post> draftedPost = postRepo.findByIsPublishedAndUserId("no",userId);
		return draftedPost;
	}
	
	public Set<Post> filterPost(String author,String tag,String date){
		author = author.trim();
		tag = tag.trim();
		date = date.trim();
		Set<Post> posts = new HashSet<Post>();
		List<Tag> postFilterByTag = tagRepo.findByName(tag);
		List<Post> postFilterByAuthor = postRepo.findByAuthor(author);
		List<Post> postFilterByDate = postRepo.findByPublishedAt(date);
		if(postFilterByTag.size() != 0) posts.addAll(postFilterByTag.get(0).getPosts());
		posts.addAll(postFilterByAuthor);
		posts.addAll(postFilterByDate);
		System.out.println(posts);
		return posts;
	}
	
	public Page<Post> searchPost(String searchText,int pageNo,int pageSize){
		searchText = searchText.trim();
		Pageable pageble = PageRequest.of(pageNo-1, pageSize);
		Page<Post> postsBySearch = postRepo.findFromSearch(pageble,searchText);
		return postsBySearch;
	}
	
	public Page<Post> sortPost(int pageNo,int pageSize,String sortType){
		Pageable pageble = PageRequest.of(pageNo-1, pageSize);
		Page<Post> sortedPost = postRepo.sortPost(pageble,sortType);
		return sortedPost;
	}
	
	public boolean updatePost(String content,String excerpt,String title,int postId) {
		boolean isUpdated = false;
		String updatedTime = new Date().toString();
		try {
			Post post = postRepo.findById(postId).get();
			post.setTitle(title);
			post.setContent(content);
			post.setExcerpt(excerpt);
			post.setUpdatedAt(updatedTime);
			postRepo.save(post);
			isUpdated = true;
			
		}catch(Exception e) {
			
		}
		return isUpdated;
	}
	
	public boolean deletePost(int postId) {
		boolean isUpdated = false;
		try {
			postRepo.deleteById(postId);
			isUpdated = true;
			
		}catch(Exception e) {
			
		}
		return isUpdated;
	}
	
	public Page<Post> findPaginated(int pageNo,int pageSize){
		Pageable pageble = PageRequest.of(pageNo-1, pageSize);
		return postRepo.findByIsPublished("yes", pageble);
	}
	
	
	
	
}

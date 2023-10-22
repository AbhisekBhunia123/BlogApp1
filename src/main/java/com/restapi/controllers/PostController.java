package com.restapi.controllers;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blogapp.entities.Post;
import com.blogapp.entities.User;
import com.blogapp.repositories.UserRepo;
import com.blogapp.services.PostServices;
import com.blogapp.services.TagServices;
import com.blogapp.services.UserServices;

@RestController
@RequestMapping("/api/account")
public class PostController {
	@Autowired
	PostServices postServices;

	@Autowired
	UserRepo userRepo;

	@Autowired
	UserServices userServices;

	@Autowired
	TagServices tagServices;

	@GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postServices.getAllPost();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUserDetails(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userServices.findByEmail(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/blog/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable("id") int id) {
        Post post = postServices.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/publishedposts/{userEmail}")
    public ResponseEntity<List<Post>> getPublishedPosts(@PathVariable("userEmail") String userEmail, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userServices.findByEmail(username);
        
        if (user.getEmail().equals(userEmail)) {
            List<Post> publishedPosts = postServices.getPublishedPost(user.getId());
            return ResponseEntity.ok(publishedPosts);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/draftedposts/{userEmail}")
    public ResponseEntity<List<Post>> getDraftedPosts(@PathVariable("userEmail") String userEmail, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userServices.findByEmail(username);
        
        if (user.getEmail().equals(userEmail)) {
            List<Post> draftedPosts = postServices.getDraftedPost(user.getId());
            return ResponseEntity.ok(draftedPosts);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/posts/page/{pageNo}")
    public ResponseEntity<Page<Post>> findPaginatedPosts(@PathVariable("pageNo") int pageNo) {
        int pageSize = 10;
        Page<Post> page = postServices.findPaginated(pageNo, pageSize);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/createpost")
    public ResponseEntity<String> createPost(@RequestParam("content") String content,
            @RequestParam("authorEmail") String authorEmail, @RequestParam("excerpt") String excerpt,
            @RequestParam("title") String title, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        boolean isSaved = postServices.createPost(title, content, excerpt, email);
        if (isSaved) {
            return ResponseEntity.ok("Post created successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create post");
        }
    }

    // Implement other POST methods similarly

    @DeleteMapping("/deleteblog/{postId}")
    public ResponseEntity<String> deleteBlog(@PathVariable("postId") int postId) {
        boolean deleted = postServices.deletePost(postId);
        if (deleted) {
            return ResponseEntity.ok("Post deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete post");
        }
    }
}

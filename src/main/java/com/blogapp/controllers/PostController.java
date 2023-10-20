package com.blogapp.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.blogapp.entities.Post;
import com.blogapp.entities.User;
import com.blogapp.repositories.UserRepo;
import com.blogapp.services.PostServices;
import com.blogapp.services.TagServices;
import com.blogapp.services.UserServices;

@Controller
public class PostController {
	@Autowired
	PostServices postServices;

	@Autowired
	UserRepo userRepo;

	@Autowired
	UserServices userServices;

	@Autowired
	TagServices tagServices;

	@GetMapping("/getpost")
	public String getAllPost(Model model) {
		List<Post> posts = postServices.getAllPost();
		model.addAttribute("posts", posts);
		return "publish";
	}

	@GetMapping("/account")
	public String getAccount(Model model) {
		User user = userRepo.findById(352).get();
		Set<String> tags = tagServices.getAllTagsName();
		Set<String> authors = userServices.getallAuthorName();
		model.addAttribute("user", user);
		model.addAttribute("authors", authors);
		model.addAttribute("tags", tags);
		return findPaginatedInAccount(model, 1);
	}

	@GetMapping("/account/blog/{id}")
	public String readPost(Model model, @PathVariable("id") int id) {
		Post post = postServices.getPostById(id);
		model.addAttribute("post", post);
		return "blog";
	}

	@GetMapping("/publishedpost/{id}")
	public String getPublishedPost(Model model, @PathVariable("id") int userId) {
		List<Post> publishedPost = postServices.getPublishedPost(userId);
		Set<String> tags = tagServices.getAllTagsName();
		Set<String> authors = userServices.getallAuthorName();
		model.addAttribute("posts", publishedPost);
		User user = userRepo.findById(userId).get();
		model.addAttribute("tags", tags);
		model.addAttribute("authors", authors);
		model.addAttribute("user", user);
		System.out.println(publishedPost);
		return "stories";
	}

	@GetMapping("/draftedpost/{id}")
	public String getDrafedPost(Model model, @PathVariable("id") int userId) {
		List<Post> draftedpost = postServices.getDraftedPost(userId);
		Set<String> tags = tagServices.getAllTagsName();
		Set<String> authors = userServices.getallAuthorName();
		model.addAttribute("posts", draftedpost);
		model.addAttribute("tags", tags);
		model.addAttribute("authors", authors);
		User user = userRepo.findById(userId).get();
		model.addAttribute("user", user);
		return "stories";
	}

	@GetMapping("account/page/{pageNo}")
	public String findPaginatedInAccount(Model model, @PathVariable("pageNo") int pageNo) {
		int pageSize = 10;
		Page<Post> page = postServices.findPaginated(pageNo, pageSize);
		List<Post> posts = page.getContent();
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("posts", posts);
		System.out.println(page.getTotalElements());
		return "account";

	}

	@PostMapping("/createpost")
	public String createPost(Model model, @RequestParam("content") String content,
			@RequestParam("excerpt") String excerpt, @RequestParam("title") String title) {

		boolean isSaved = postServices.createPost(title, content, excerpt);
		if (isSaved) {
			return "redirect:/account";
		}
		return "post";
	}

	@PostMapping("/publishpost")
	public String publishPost(Model model, @RequestParam("content") String content,
			@RequestParam("excerpt") String excerpt, @RequestParam("title") String title) {
		boolean isPublished = postServices.publishPost(title, content, excerpt);
		if (isPublished) {
			return "redirect:/account";
		}
		return "post";
	}

	@PostMapping("/publishdraftpost")
	public String publishDraftPost(@RequestParam("postId") int postId, @RequestParam("userId") int userId) {
		boolean isPublished = postServices.publishPost(postId);
		if (isPublished) {
			return "redirect:/publishedpost/" + userId;
		}
		return "redirect:/draftedpost/" + userId;
	}

	@PostMapping("/updatepost")
	public String updatePost(Model model, @RequestParam("postId") int postId) {
		Post post = postServices.getPostById(postId);
		model.addAttribute("post", post);
		return "post";
	}

	@PostMapping("/updateblog")
	public String updateBlog(Model model, @RequestParam("postId") int postId, @RequestParam("content") String content,
			@RequestParam("excerpt") String excerpt, @RequestParam("title") String title) {
		postServices.updatePost(content, excerpt, title, postId);

		return "redirect:/account";
	}

	@PostMapping("/deleteblog")
	public String deleteBlog(Model model, @RequestParam("postId") int postId) {
		postServices.deletePost(postId);

		return "redirect:/account";
	}

}

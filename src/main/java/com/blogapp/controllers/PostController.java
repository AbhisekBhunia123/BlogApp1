package com.blogapp.controllers;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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

	@GetMapping("/account/getpost")
	public String getAllPost(Model model) {
		List<Post> posts = postServices.getAllPost();
		model.addAttribute("posts", posts);
		return "publish";
	}

	@GetMapping("/account")
	public String getAccount(Model model, Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		User user = userServices.findByEmail(username);
		Set<String> tags = tagServices.getAllTagsName();
		Set<String> authors = userServices.getallAuthorName();

		model.addAttribute("user", user);
		model.addAttribute("authors", authors);
		model.addAttribute("tags", tags);
		return findPaginatedInAccount(model, 1);
	}

	@GetMapping("/blog/{id}")
	public String readPost(Model model, @PathVariable("id") int id) {
		Post post = postServices.getPostById(id);
		model.addAttribute("post", post);
		return "blog";

	}

	@GetMapping("/account/publishedpost/{id}")
	public String getPublishedPost(Model model, @PathVariable("id") String userEmail, Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		User user = userServices.findByEmail(username);
		System.out.println(userEmail);
		if (user.getEmail().equals(userEmail)) {
			List<Post> publishedPost = postServices.getPublishedPost(user.getId());
			Set<String> tags = tagServices.getAllTagsName();
			Set<String> authors = userServices.getallAuthorName();
			model.addAttribute("posts", publishedPost);
			model.addAttribute("tags", tags);
			model.addAttribute("authors", authors);
			model.addAttribute("user", user);
			System.out.println(publishedPost);
			return "stories";
		}
		return "redirect:/account";
	}

	@GetMapping("/account/draftedpost/{id}")
	public String getDrafedPost(Model model, @PathVariable("id") String userEmail, Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		User user = userServices.findByEmail(username);

		if (user.getEmail().equals(userEmail)) {
			List<Post> draftedpost = postServices.getDraftedPost(user.getId());
			Set<String> tags = tagServices.getAllTagsName();
			Set<String> authors = userServices.getallAuthorName();
			model.addAttribute("posts", draftedpost);
			model.addAttribute("tags", tags);
			model.addAttribute("authors", authors);
			model.addAttribute("user", user);
			return "stories";
		}
		return "redirect:/account";
	}

	@GetMapping("/account/page/{pageNo}")
	public String findPaginatedInAccount(Model model, @PathVariable("pageNo") int pageNo) {
		int pageSize = 10;
		Page<Post> page = postServices.findPaginated(pageNo, pageSize);
		List<Post> posts = page.getContent();
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("posts", posts);
		model.addAttribute("simplePaging", true);
		System.out.println(page.getTotalElements());
		return "account";

	}

	@PostMapping("/account/createpost")
	public String createPost(Model model, @RequestParam("content") String content,
			@RequestParam("authorEmail") String authorEmail, @RequestParam("excerpt") String excerpt,
			@RequestParam("title") String title, Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String email = userDetails.getUsername();
		boolean isSaved = postServices.createPost(title, content, excerpt, email);
		if (isSaved) {
			return "redirect:/account";
		}
		return "post";
	}

	@PostMapping("/account/publishpost")
	public String publishPost(Model model, @RequestParam("content") String content,
			@RequestParam("authorEmail") String authorEmail, @RequestParam("excerpt") String excerpt,
			@RequestParam("title") String title, Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String email = userDetails.getUsername();
		Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
		boolean isAdmin = authorities.stream().anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
		boolean isPublished = postServices.publishPost(title, content, excerpt, email, isAdmin, authorEmail);
		if (isPublished) {
			return "redirect:/account";
		}
		return "post";
	}

	@PostMapping("/account/publishdraftpost")
	public String publishDraftPost(@RequestParam("postId") int postId, @RequestParam("userId") String userEmail) {
		boolean isPublished = postServices.publishPost(postId);
		if (isPublished) {
			return "redirect:/account/publishedpost/" + userEmail;
		}
		return "redirect:/account/draftedpost/" + userEmail;
	}

	@PostMapping("/account/updatepost")
	public String updatePost(Model model, @RequestParam("postId") int postId) {
		Post post = postServices.getPostById(postId);
		model.addAttribute("post", post);
		return "post";
	}

	@PostMapping("/account/updateblog")
	public String updateBlog(Model model, @RequestParam("postId") int postId, @RequestParam("content") String content,
			@RequestParam("excerpt") String excerpt, @RequestParam("title") String title) {
		System.out.println("Hi");
		postServices.updatePost(content, excerpt, title, postId);

		return "redirect:/account";
	}

	@PostMapping("/account/deleteblog")
	public String deleteBlog(Model model, @RequestParam("postId") int postId) {
		postServices.deletePost(postId);

		return "redirect:/account";
	}

}

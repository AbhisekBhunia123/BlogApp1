package com.blogapp.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.blogapp.entities.Post;
import com.blogapp.entities.User;
import com.blogapp.services.PostServices;
import com.blogapp.services.TagServices;
import com.blogapp.services.UserServices;

@Controller
public class ViewController {

	@Autowired
	UserServices userServices;

	@Autowired
	PostServices postServices;

	@Autowired
	TagServices tagService;

	@GetMapping("/")
	public String home(Model model) {
		return findPaginated(model, 1);
	}

	@GetMapping("/account/post")
	public String post() {
		return "post";
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	@GetMapping("/showLoginPage")
	public String login(Authentication authentication) {
		if (authentication != null) {
			return "redirect:/account";
		}
		return "login";
	}

	@PostMapping("/account/admincontroll")
	public String showAdminController(@RequestParam("postId") int postId, Model model) {
		Post post = postServices.getPostById(postId);
		model.addAttribute("post", post);
		return "adminControllPage";
	}

	@GetMapping("/page/{pageNo}")
	public String findPaginated(Model model, @PathVariable("pageNo") int pageNo) {
		int pageSize = 4;
		Page<Post> page = postServices.findPaginated(pageNo, pageSize);
		Set<String> tags = tagService.getAllTagsName();
		Set<String> authors = userServices.getallAuthorName();
		List<Post> posts = page.getContent();
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("posts", posts);
		model.addAttribute("tags", tags);
		model.addAttribute("authors", authors);
		model.addAttribute("searchResults", null);
		model.addAttribute("simplePaging", true);
		model.addAttribute("searchPaging", false);
		model.addAttribute("filterPage", false);
		model.addAttribute("sortPaging", false);
		return "home";

	}

	@GetMapping(value = "/search")
	public String search(Model model, @RequestParam("search") String search, @RequestParam("pageNo") int pageNo,
			Authentication authentication) {
		int pageSize = 4;
		Set<Post> searchPosts = postServices.searchPostPage(search, pageNo, pageSize);
		Set<String> tags = tagService.getAllTagsName();
		Set<String> authors = userServices.getallAuthorName();
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", (searchPosts.size() / pageSize) + 1);
		model.addAttribute("totalItems", searchPosts.size());
		model.addAttribute("posts", searchPosts);
		model.addAttribute("tags", tags);
		model.addAttribute("authors", authors);
		model.addAttribute("searchText", search);
		model.addAttribute("simplePaging", false);
		model.addAttribute("searchPaging", true);
		model.addAttribute("filterPage", false);
		model.addAttribute("sortPaging", false);
		if (authentication == null) {
			return "home";
		} else {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String username = userDetails.getUsername();
			User user = userServices.findByEmail(username);
			model.addAttribute("user", user);
			return "account";
		}
	}

	@GetMapping(value = "/sortpost")
	public String sortPost(Model model, @RequestParam("sortValue") String sortType, @RequestParam("pageNo") int pageNo,
			Authentication authentication) {
		int pageSize = 4;
		Page<Post> page = postServices.sortPost(pageNo, pageSize, sortType);
		Set<String> tags = tagService.getAllTagsName();
		Set<String> authors = userServices.getallAuthorName();
		List<Post> posts = page.getContent();
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("posts", posts);
		model.addAttribute("tags", tags);
		model.addAttribute("authors", authors);
		model.addAttribute("simplePaging", false);
		model.addAttribute("searchPaging", false);
		model.addAttribute("filterPage", false);
		model.addAttribute("sortPaging", true);
		model.addAttribute("sortType", sortType);
		if (authentication == null) {
			return "home";
		} else {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String username = userDetails.getUsername();
			User user = userServices.findByEmail(username);
			model.addAttribute("user", user);
			return "account";
		}
	}

	@PostMapping("/homefilter")
	public String processForm(Model model, @RequestParam("searchText") String searchText,
			@RequestParam(value = "authors", required = false) String[] authors,
			@RequestParam(value = "tags", required = false) String[] tags, @RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate, @RequestParam("pageNo") int pageNo,
			Authentication authentication) {
		int pageSize = 4;
		System.out.println(searchText);
		Set<Post> posts = postServices.filterPost(authors, tags, startDate, endDate, pageNo, pageSize, searchText);
		Set<String> navtags = tagService.getAllTagsName();
		Set<String> navauthor = userServices.getallAuthorName();
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", (posts.size() / pageSize) + 1);
		model.addAttribute("totalItems", posts.size());
		model.addAttribute("posts", posts);
		model.addAttribute("tags", navtags);
		model.addAttribute("authors", navauthor);
		model.addAttribute("searchResults", null);
		model.addAttribute("simplePaging", false);
		model.addAttribute("searchPaging", false);
		model.addAttribute("filterPage", true);
		model.addAttribute("sortPaging", false);
		model.addAttribute("filterTags", tags);
		model.addAttribute("filterAuthor", authors);
		model.addAttribute("StartDate", startDate);
		model.addAttribute("endDate", endDate);
		if (authentication == null) {
			return "home";
		} else {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String username = userDetails.getUsername();
			User user = userServices.findByEmail(username);
			model.addAttribute("user", user);
			return "account";
		}
	}

}

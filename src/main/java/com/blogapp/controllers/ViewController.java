package com.blogapp.controllers;


import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.blogapp.entities.Post;
import com.blogapp.entities.User;
import com.blogapp.repositories.PostRepo;
import com.blogapp.repositories.UserRepo;
import com.blogapp.services.PostServices;

@Controller
public class ViewController {

	@Autowired
	UserRepo userRepo;
	
	@Autowired
	PostServices postServices;
	
	@RequestMapping("/")
	public String home(Model model) {
//		List<Post> posts = postServices.getAllPost();
//		model.addAttribute("posts",posts);
		return findPaginated(model,1);
	}
	
	@RequestMapping(value = "/post",method = RequestMethod.GET)
	public String post() {
		return "post";
	}
	
	@RequestMapping(value = "/register",method = RequestMethod.GET)
	public String register() {
		return "redirect:/";
	}
	
	@RequestMapping(value = "/login",method = RequestMethod.GET)
	public String login() {
		return "redirect:/";
	}
	
	@RequestMapping(value = "/homefilter",method = RequestMethod.POST)
	public String filter(Model model,@RequestParam("author") String author,@RequestParam("tag") String tag,@RequestParam("date") String date) {
		System.out.println(author);
		Set<Post> posts = postServices.filterPost(author, tag, date);
		model.addAttribute("posts",posts);
		return "home";
	}
	
	@RequestMapping(value = "/blog")
	public String search(Model model,@RequestParam("search") String search) {
		int pageSize = 10;
		Page<Post> page = postServices.searchPost(search,1, pageSize);
		List<Post> posts = page.getContent();
		model.addAttribute("currentPage",1);
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("totalItems",page.getTotalElements());
		model.addAttribute("posts",posts);
		System.out.println(page.getTotalElements());
		return "home";
	}
	
	@RequestMapping(value="/sortpost")
	public String sortPost(Model model,@RequestParam("sortValue") String sortType) {
		int pageSize = 10;
		Page<Post> page = postServices.sortPost(1,pageSize,sortType);
		List<Post> posts = page.getContent();
		model.addAttribute("currentPage",1);
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("totalItems",page.getTotalElements());
		model.addAttribute("posts",posts);
		return "home";
	}
	
	@GetMapping("/page/{pageNo}")
	public String findPaginated(Model model,@PathVariable("pageNo") int pageNo) {
		int pageSize = 10;
		Page<Post> page = postServices.findPaginated(pageNo, pageSize);
		List<Post> posts = page.getContent();
		model.addAttribute("currentPage",pageNo);
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("totalItems",page.getTotalElements());
		model.addAttribute("posts",posts);
		System.out.println(page.getTotalElements());
		return "home";
		
	}
	
	
}

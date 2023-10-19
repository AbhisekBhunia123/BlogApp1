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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.blogapp.entities.Post;
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
	
	@RequestMapping("/")
	public String home(Model model) {
		return findPaginated(model,1);
	}
	
	@RequestMapping(value = "/post",method = RequestMethod.GET)
	public String post() {
		return "post";
	}
	
	@RequestMapping(value = "/register",method = RequestMethod.GET)
	public String register() {
		return "register";
	}
	
	@RequestMapping(value = "/showLoginPage",method = RequestMethod.GET)
	public String login() {
		return "login";
	}
	
	@PostMapping("/homefilter")
    public String processForm(Model model,
        @RequestParam(value = "authors", required = false) String[] authors,
        @RequestParam(value = "tags", required = false) String[] tags,@RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate,@RequestParam("pageNo") int pageNo) {
		int pageSize = 4;
		Set<Post> posts = postServices.filterPost(authors,tags,startDate,endDate,pageNo,pageSize);
		Set<String> navtags = tagService.getAllTagsName();
		Set<String> navauthor = userServices.getallAuthorName();
		model.addAttribute("currentPage",pageNo);
		model.addAttribute("totalPages",(posts.size()/pageSize)+1);
		model.addAttribute("totalItems",posts.size());
		model.addAttribute("posts",posts);
		model.addAttribute("tags",navtags);
		model.addAttribute("authors",navauthor);
		model.addAttribute("searchResults",null);
		model.addAttribute("filterPage",true);
		model.addAttribute("filterTags",tags);
		model.addAttribute("filterAuthor",authors);
		model.addAttribute("StartDate",startDate);
		model.addAttribute("endDate",endDate);
        return "home";
    }
	
	
	@RequestMapping(value = "/search")
	public String search(Model model,@RequestParam("search") String search,@RequestParam("pageNo") int pageNo) {
		postServices.searchPost(search);
		int pageSize = 2;
		Set<Post> searchPosts = postServices.searchPostPage(search,pageNo,pageSize);
		Set<String> tags = tagService.getAllTagsName();
		Set<String> authors = userServices.getallAuthorName();
		model.addAttribute("currentPage",pageNo);
		model.addAttribute("totalPages",(searchPosts.size()/pageSize)+1);
		model.addAttribute("totalItems",searchPosts.size());
		model.addAttribute("posts",searchPosts);
		model.addAttribute("tags",tags);
		model.addAttribute("authors",authors);
		model.addAttribute("searchText",search);
		model.addAttribute("searchPaging",true);
		return "home";
	}
	
	
	

	
	
	@RequestMapping(value="/sortpost")
	public String sortPost(Model model,@RequestParam("sortValue") String sortType,@RequestParam("pageNo") int pageNo) {
		int pageSize = 2;
		Page<Post> page = postServices.sortPost(pageNo,pageSize,sortType);
		Set<String> tags = tagService.getAllTagsName();
		Set<String> authors = userServices.getallAuthorName();
		List<Post> posts = page.getContent();
		model.addAttribute("currentPage",pageNo);
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("totalItems",page.getTotalElements());
		model.addAttribute("posts",posts);
		model.addAttribute("tags",tags);
		model.addAttribute("authors",authors);
		model.addAttribute("sortPaging",true);
		model.addAttribute("sortType",sortType);
		return "home";
	}
	
	
	
	@GetMapping("/page/{pageNo}")
	public String findPaginated(Model model,@PathVariable("pageNo") int pageNo) {
		int pageSize = 2;
		Page<Post> page = postServices.findPaginated(pageNo, pageSize);
		Set<String> tags = tagService.getAllTagsName();
		Set<String> authors = userServices.getallAuthorName();
		List<Post> posts = page.getContent();
		model.addAttribute("currentPage",pageNo);
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("totalItems",page.getTotalElements());
		model.addAttribute("posts",posts);
		model.addAttribute("tags",tags);
		model.addAttribute("authors",authors);
		model.addAttribute("searchResults",null);
		model.addAttribute("simplePaging",true);
		System.out.println(page.getTotalElements());
		return "home";
		
	}
	
	
}

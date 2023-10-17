package com.blogapp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.blogapp.entities.Post;
import com.blogapp.entities.User;
import com.blogapp.repositories.UserRepo;
import com.blogapp.services.PostServices;
import com.blogapp.services.UserServices;

@Controller
public class PostController {
	@Autowired
	PostServices postServices;
	
	@Autowired
	UserRepo userRepo;
	
	
	@RequestMapping(value = "/createpost",method = RequestMethod.POST)
	public String createPost(Model model,@RequestParam("content") String content,@RequestParam("excerpt") String excerpt,@RequestParam("title") String title) {
		boolean isSaved = postServices.createPost(title,content,excerpt);
		if(isSaved) {
			return "redirect:/account";
		}
		return "post";
	}
	
	@RequestMapping(value = "/publishpost",method = RequestMethod.POST)
	public String publishPost(Model model, @RequestParam("content") String content,@RequestParam("excerpt") String excerpt,@RequestParam("title") String title) {
		boolean isPublished = postServices.publishPost(title,content,excerpt);
		if(isPublished) {
			return "redirect:/account";
		}
		return "post";
	}
	
	@RequestMapping(value = "/publishdraftpost",method = RequestMethod.POST)
	public String publishDraftPost(@RequestParam("postId") int postId,@RequestParam("userId") int userId) {
		boolean isPublished = postServices.publishPost(postId);
		if(isPublished) {
			return "redirect:/publishedpost/"+userId;
		}
		return "redirect:/draftedpost/"+userId;
	}
	
	@RequestMapping(value = "/getpost",method = RequestMethod.GET)
	public String getAllPost(Model model) {
		List<Post> posts = postServices.getAllPost();
		model.addAttribute("posts", posts);
		return "publish";
	}
	
	
	@RequestMapping(value = "/account")
	public String getAccount(Model model) {
		User user = userRepo.findById(252).get();
		model.addAttribute("user",user);
		return findPaginatedInAccount(model,1);
	}
	
	@RequestMapping(value = "/account/blog/{id}",method = RequestMethod.GET)
	public String readPost(Model model,@PathVariable("id") int id) {
		Post post = postServices.getPostById(id);
		model.addAttribute("post",post);
		return "blog";
	}

	@RequestMapping(value = "/publishedpost/{id}")
	public String getPublishedPost(Model model,@PathVariable("id") int userId) {
		List<Post> publishedPost = postServices.getPublishedPost(userId);
        model.addAttribute("posts", publishedPost);
        User user = userRepo.findById(userId).get();
		model.addAttribute("user",user);
        System.out.println(publishedPost);
		return "stories";
	}
	
	@RequestMapping(value = "/draftedpost/{id}")
	public String getDrafedPost(Model model,@PathVariable("id") int userId) {
		List<Post> draftedpost = postServices.getDraftedPost(userId);
        model.addAttribute("posts", draftedpost);
        User user = userRepo.findById(userId).get();
		model.addAttribute("user",user);
		return "stories";
	}
	
	@RequestMapping(value = "/updatepost",method = RequestMethod.POST)
	public String updatePost(Model model,@RequestParam("postId") int postId) {
		Post post = postServices.getPostById(postId);
		model.addAttribute("post",post);
		return "post";
	}
	
	@RequestMapping(value = "/updateblog",method = RequestMethod.POST)
	public String updateBlog(Model model,@RequestParam("postId") int postId,@RequestParam("content") String content,@RequestParam("excerpt") String excerpt,@RequestParam("title") String title) {
		boolean isUpdated = postServices.updatePost(content,excerpt,title, postId);
		
		return "redirect:/account";
	}
	
	
	@RequestMapping(value = "/deleteblog",method = RequestMethod.POST)
	public String deleteBlog(Model model,@RequestParam("postId") int postId) {
		boolean isUpdated = postServices.deletePost(postId);
		
		return "redirect:/account";
	}
	
	@GetMapping("account/page/{pageNo}")
	public String findPaginatedInAccount(Model model,@PathVariable("pageNo") int pageNo) {
		int pageSize = 10;
		Page<Post> page = postServices.findPaginated(pageNo, pageSize);
		List<Post> posts = page.getContent();
		model.addAttribute("currentPage",pageNo);
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("totalItems",page.getTotalElements());
		model.addAttribute("publishedPost",posts);
		System.out.println(page.getTotalElements());
		return "account";
		
	}
	
}

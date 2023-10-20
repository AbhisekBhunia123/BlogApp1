package com.blogapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.blogapp.services.TagServices;

@Controller
public class TagController {
	@Autowired
	TagServices tagServices;

	@PostMapping("/createtag")
	public String createTag(@RequestParam("name") String name, @RequestParam("postId") int postId,
			@RequestParam("userId") int userId) {
		tagServices.createTag(name, postId);
		System.out.println(postId);
		return "redirect:/publishedpost/" + userId;
	}

	@PostMapping("/updatetag")
	public String updatetag(@RequestParam("newname") String newTagName, @RequestParam("oldname") String oldTagName,
			@RequestParam("postId") int postId, @RequestParam("userId") int userId) {
		tagServices.updateTag(oldTagName, newTagName, postId);
		return "redirect:/publishedpost/" + userId;
	}

	@PostMapping("/deltag")
	public String deleteTag(@RequestParam("name") String name, @RequestParam("userId") int userId,
			@RequestParam("postId") int postId) {
		tagServices.deleteTag(name, postId);
		return "redirect:/publishedpost/" + userId;
	}

}

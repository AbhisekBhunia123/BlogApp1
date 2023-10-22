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

	@PostMapping("/account/createtag")
	public String createTag(@RequestParam("name") String name, @RequestParam("postId") int postId,
			@RequestParam("userId") String userMail) {
		tagServices.createTag(name, postId);
		return "redirect:/account/publishedpost/" + userMail;
	}

	@PostMapping("/account/updatetag")
	public String updatetag(@RequestParam("newname") String newTagName, @RequestParam("oldname") String oldTagName,
			@RequestParam("postId") int postId, @RequestParam("userId") String userEmail) {
		tagServices.updateTag(oldTagName, newTagName, postId);
		return "redirect:/account/publishedpost/" + userEmail;
	}

	@PostMapping("/account/deltag")
	public String deleteTag(@RequestParam("name") String name, @RequestParam("userId") String userEmail,
			@RequestParam("postId") int postId) {
		tagServices.deleteTag(name, postId);
		return "redirect:/account/publishedpost/" + userEmail;
	}

}

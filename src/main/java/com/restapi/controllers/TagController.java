package com.restapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blogapp.services.TagServices;

@RestController
@RequestMapping("/api/account")
public class TagController {
    
    @Autowired
    private TagServices tagServices;

    @PostMapping("/createtag")
    public ResponseEntity<String> createTag(@RequestParam("name") String name, @RequestParam("postId") int postId) {
        tagServices.createTag(name, postId);
        return ResponseEntity.ok("Tag created successfully");
    }

    @PutMapping("/updatetag")
    public ResponseEntity<String> updateTag(@RequestParam("newname") String newTagName,
                                           @RequestParam("oldname") String oldTagName,
                                           @RequestParam("postId") int postId) {
        tagServices.updateTag(oldTagName, newTagName, postId);
        return ResponseEntity.ok("Tag updated successfully");
    }

    @DeleteMapping("/deltag")
    public ResponseEntity<String> deleteTag(@RequestParam("name") String name,
                                           @RequestParam("postId") int postId) {
        tagServices.deleteTag(name, postId);
        return ResponseEntity.ok("Tag deleted successfully");
    }
}

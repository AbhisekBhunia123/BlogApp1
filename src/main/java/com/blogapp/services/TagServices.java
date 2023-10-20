package com.blogapp.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blogapp.entities.Post;
import com.blogapp.entities.Tag;
import com.blogapp.repositories.PostRepo;
import com.blogapp.repositories.TagRepo;

@Service
public class TagServices {

	@Autowired
	TagRepo tagRepo;

	@Autowired
	PostRepo postRepo;

	public boolean createTag(String name, int postId) {
		boolean isCreated = false;
		String createdAt = new Date().toString();
		String updatedAt = new Date().toString();
		try {
			Optional<Post> posts = postRepo.findById(postId);
			Post post = posts.get();
			if (checkIfNewExist(post.getTags(), name) != null) {
				return false;
			}
			List<Post> list = new ArrayList<>();
			Tag tag = new Tag();
			tag.setName(name);
			tag.setCreatedAt(createdAt);
			tag.setUpdatedAt(updatedAt);
			list.add(post);
			tag.setPosts(list);
			Tag tag1 = tagRepo.save(tag);
			post.getTags().add(tag1);
			postRepo.save(post);
			isCreated = true;
		} catch (Exception e) {
			System.out.println(e);
		}
		return isCreated;

	}

	public boolean updateTag(String oldTagName, String newTagName, int postId) {
		boolean isCreated = false;
		String updatedAt = new Date().toString();
		try {

			Post post = postRepo.findById(postId).get();
			List<Tag> tags = post.getTags();
			if (checkIfNewExist(tags, newTagName) != null) {
				return false;
			} else {
				for (Tag tag : tags) {
					if (tag.getName().equals(oldTagName)) {
						tag.setName(newTagName);
						tag.setUpdatedAt(updatedAt);
						tagRepo.save(tag);
						postRepo.save(post);
						return true;
					}
				}
			}

		} catch (Exception e) {

		}
		return isCreated;

	}

	public boolean deleteTag(String name, int postId) {
		boolean isDeleted = false;
		try {
			Post post = postRepo.findById(postId).get();
			List<Tag> tags = post.getTags();
			for (Tag tag : tags) {
				if (tag.getName().equals(name)) {
					tags.remove(tag);
					postRepo.save(post);
					tagRepo.delete(tag);
					return true;
				}
			}
		} catch (Exception e) {

		}
		return isDeleted;
	}

	public Tag checkIfNewExist(List<Tag> tags, String newTagName) {
		for (Tag tag : tags) {
			if (tag.getName().equals(newTagName)) {
				return tag;
			}
		}
		return null;
	}

	public Set<String> getAllTagsName() {
		List<Tag> tags = tagRepo.findAll();
		Set<String> tagsName = new HashSet<>();
		for (int index = 0; index < tags.size(); index++) {
			String tagName = tags.get(index).getName();
			tagsName.add(tagName);
		}
		return tagsName;
	}
}

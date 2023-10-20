package com.blogapp.services;

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
		Date createdAt = new Date();
		Date updatedAt = new Date();
		try {
			Optional<Post> posts = postRepo.findById(postId);
			List<Tag> existingTags = tagRepo.findByName(name);
			Post post = posts.get();
			List<Tag> tags = post.getTags();
			if (checkIfNewExist(tags, name) != null) {
				return false;
			}
			if (existingTags.size() != 0) {
				tags.add(existingTags.get(0));
				postRepo.save(post);
			} else {
				Tag newtag = new Tag();
				newtag.setName(name);
				newtag.setCreatedAt(createdAt);
				newtag.setUpdatedAt(updatedAt);
				Tag tag1 = tagRepo.save(newtag);
				tags.add(tag1);
				postRepo.save(post);
			}

			isCreated = true;
		} catch (Exception e) {
			System.out.println(e);
		}
		return isCreated;

	}

	public boolean updateTag(String oldTagName, String newTagName, int postId) {
		boolean isCreated = false;
		try {

			Post post = postRepo.findById(postId).get();
			List<Tag> tags = post.getTags();
			if (checkIfNewExist(tags, newTagName) != null) {
				return false;
			} else {
				for (Tag tag : tags) {
					if (tag.getName().equals(oldTagName)) {
						createTag(newTagName, postId);
						deleteTag(oldTagName, postId);
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
				if (tag.getName().equals(name) && tag.getPosts().size() == 1) {
					System.out.println("Deleted");
					tags.remove(tag);
					postRepo.save(post);
					return true;
				} else if (tag.getName().equals(name) && tag.getPosts().size() != 1) {
					System.out.println("simple delete");
					tags.remove(tag);
					postRepo.save(post);
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

package com.blogapp.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.blogapp.entities.Post;
import com.blogapp.entities.Tag;
import com.blogapp.entities.User;
import com.blogapp.repositories.PostRepo;
import com.blogapp.repositories.TagRepo;
import com.blogapp.repositories.UserRepo;

@Component
public class PostServices {

	@Autowired
	PostRepo postRepo;

	@Autowired
	TagRepo tagRepo;

	@Autowired
	UserRepo userRepo;

	public boolean createPost(String title, String content, String excerpt) {
		boolean isCreated = false;
		try {
			String createdAt = new Date().toString();
			String updatedAt = new Date().toString();
			Post post = new Post();
			post.setAuthor("Abhisek");
			post.setContent(content);
			post.setTitle(title);
			post.setExcerpt(excerpt);
			post.setIsPublished("no");
			post.setPublishedAt("none");
			post.setCreatedAt(createdAt);
			post.setUpdatedAt(updatedAt);
			User user = userRepo.findById(352).get();
			post.setUser(user);
			postRepo.save(post);
			isCreated = true;
		} catch (Exception e) {

		}
		return isCreated;

	}

	public boolean publishPost(String title, String content, String excerpt) {
		boolean isCreated = false;
		try {
			String createdAt = new Date().toString();
			String updatedAt = new Date().toString();
			String publishedAt = new Date().toString();
			Post post = new Post();
			post.setAuthor("Abhisek Bhunia");
			post.setContent(content);
			post.setTitle(title);
			post.setExcerpt(excerpt);
			post.setIsPublished("yes");
			post.setPublishedAt(publishedAt);
			post.setCreatedAt(createdAt);
			post.setUpdatedAt(updatedAt);
			User user = userRepo.findById(352).get();
			post.setUser(user);
			postRepo.save(post);
			isCreated = true;
		} catch (Exception e) {

		}
		return isCreated;

	}

	public boolean publishPost(int postId) {
		boolean isPublished = false;
		try {
			Post post = postRepo.findById(postId).get();
			post.setIsPublished("yes");
			postRepo.save(post);
			isPublished = true;

		} catch (Exception e) {

		}
		return isPublished;
	}

	public Post getPostById(int id) {
		Post post = postRepo.findById(id).get();
		return post;
	}

	public List<Post> getAllPost() {
		List<Post> posts = new ArrayList<>();
		posts = postRepo.findAll();
		return posts;

	}

	public List<Post> getDraftPost() {
		List<Post> draftPosts = new ArrayList<>();
		draftPosts = postRepo.findByIsPublished("no");
		return draftPosts;

	}

	public List<Post> getPublishedPost() {
		List<Post> publishedPost = new ArrayList<>();
		publishedPost = postRepo.findByIsPublished("yes");
		return publishedPost;

	}

	public List<Post> getPublishedPost(int userId) {
		List<Post> publishedPost = postRepo.findByIsPublishedAndUserId("yes", userId);
		return publishedPost;
	}

	public List<Post> getDraftedPost(int userId) {
		List<Post> draftedPost = postRepo.findByIsPublishedAndUserId("no", userId);
		return draftedPost;
	}

	public Set<Post> filterPost(String[] authors, String[] tags, String startDate, String endDate, int pageNo,
			int pageSize) {
		ArrayList<String> authorsList = new ArrayList<>();
		List<String> tagsList = new ArrayList<>();
		Set<Post> posts = new HashSet<>();
		Pageable pageble = PageRequest.of(pageNo - 1, pageSize);
		if (authors != null) {
			authorsList.addAll(Arrays.asList(authors));
		}
		if (tags != null) {
			tagsList.addAll(Arrays.asList(tags));
		}
		Page<Post> authorposts = null;
		Page<Post> tagPosts = null;
		if (authorsList.size() != 0 && tagsList.size() == 0 && startDate.length() == 0 && endDate.length() == 0) {
			authorposts = postRepo.findByAuthorIn(authorsList, pageble);
			posts.addAll(authorposts.getContent());
		} else if (authorsList.size() == 0 && tagsList.size() != 0 && startDate.length() == 0
				&& endDate.length() == 0) {
			tagPosts = tagRepo.findByTagNamesIn(tagsList, pageble);
			posts.addAll(tagPosts.getContent());
		} else if (authorsList.size() == 0 && tagsList.size() == 0 && startDate.length() != 0
				&& endDate.length() != 0) {
			authorposts = postRepo.findByCreatedAtBetween(startDate, endDate, pageble);
			posts.addAll(authorposts.getContent());
		} else if (authorsList.size() != 0 && tagsList.size() == 0 && startDate.length() != 0
				&& endDate.length() != 0) {
			authorposts = postRepo.findByAuthorInAndCreatedAtBetween(authorsList, startDate, endDate, pageble);
			posts.addAll(authorposts.getContent());
		} else if (authorsList.size() != 0 && tagsList.size() != 0 && startDate.length() != 0
				&& endDate.length() != 0) {
			authorposts = postRepo.filterByAuthorTagAndCreatedAt(authorsList, tagsList, startDate, endDate, pageble);
			posts.addAll(authorposts.getContent());
		} else if (authorsList.size() == 0 && tagsList.size() != 0 && startDate.length() != 0
				&& endDate.length() != 0) {
			authorposts = postRepo.filterByTagNameAndCreatedAt(tagsList, startDate, endDate, pageble);
			posts.addAll(authorposts.getContent());
		} else if (authorsList.size() != 0 && tagsList.size() != 0 && startDate.length() == 0
				&& endDate.length() == 0) {
			authorposts = postRepo.filterByTagNameAndAuthor(authorsList, tagsList, pageble);
			posts.addAll(authorposts.getContent());
		}
		return posts;
	}

	public Set<Post> searchPost(String searchText) {
		searchText = searchText.trim();
		String searchTexts[] = searchText.split(" ");
		Set<Post> searchPosts = new HashSet<>();
		for (String text : searchTexts) {
			List<Post> postsBySearch = postRepo.findFromSearch(text);
			searchPosts.addAll(postsBySearch);
		}

		return searchPosts;
	}

	public Set<Post> searchPostPage(String searchText, int pageNo, int pageSize) {
		searchText = searchText.trim();
		String searchTexts[] = searchText.split(" ");
		Set<Post> searchPosts = new HashSet<>();
		Pageable pageble = PageRequest.of(pageNo - 1, pageSize);
		for (String text : searchTexts) {
			Page<Post> postsBySearch = postRepo.findFromSearchPage(pageble, text);
			searchPosts.addAll(postsBySearch.getContent());
		}

		return searchPosts;
	}

	public Page<Post> sortPost(int pageNo, int pageSize, String sortType) {
		PageRequest pageable = null;
		Page<Post> sortedPost = null;
		if (sortType.equals("asc")) {
			pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.ASC, "publishedAt"));
			sortedPost = postRepo.findAllByOrderByCreatedAtAsc(pageable);
		} else {
			pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.DESC, "publishedAt"));
			sortedPost = postRepo.findAllByOrderByCreatedAtDesc(pageable);
		}

		return sortedPost;
	}

	public boolean updatePost(String content, String excerpt, String title, int postId) {
		boolean isUpdated = false;
		String updatedTime = new Date().toString();
		try {
			Post post = postRepo.findById(postId).get();
			post.setTitle(title);
			post.setContent(content);
			post.setExcerpt(excerpt);
			post.setUpdatedAt(updatedTime);
			postRepo.save(post);
			isUpdated = true;

		} catch (Exception e) {

		}
		return isUpdated;
	}

	public boolean deletePost(int postId) {
		boolean isUpdated = false;
		try {
			postRepo.deleteById(postId);
			isUpdated = true;

		} catch (Exception e) {

		}
		return isUpdated;
	}

	public Page<Post> findPaginated(int pageNo, int pageSize) {
		Pageable pageble = PageRequest.of(pageNo - 1, pageSize);
		return postRepo.findByIsPublished("yes", pageble);
	}

}

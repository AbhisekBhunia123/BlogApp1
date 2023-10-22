package com.blogapp.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blogapp.dao.PostDao;
import com.blogapp.entities.Post;
import com.blogapp.entities.User;
import com.blogapp.repositories.PostRepo;
import com.blogapp.repositories.TagRepo;
import com.blogapp.repositories.UserRepo;

@Service
public class PostServices {

	@Autowired
	PostRepo postRepo;

	@Autowired
	TagRepo tagRepo;

	@Autowired
	UserRepo userRepo;

	@Autowired
	PostDao postDao;

	public boolean createPost(String title, String content, String excerpt, String email) {
		boolean isCreated = false;
		Date createdAt = new Date();
		Date updatedAt = new Date();
		try {
			User user = userRepo.findByEmail(email);
			Post post = new Post();
			post.setAuthor(user.getName());
			post.setContent(content);
			post.setTitle(title);
			post.setExcerpt(excerpt);
			post.setIsPublished("no");
			post.setPublishedAt(null);
			post.setCreatedAt(createdAt);
			post.setUpdatedAt(updatedAt);
			post.setUser(user);
			postRepo.save(post);
			isCreated = true;
		} catch (Exception e) {
			System.out.println(e);
		}
		return isCreated;

	}

	public boolean publishPost(String title, String content, String excerpt, String email, boolean isAdmin,
			String authorEmail) {
		boolean isCreated = false;
		Date createdAt = new Date();
		Date updatedAt = new Date();
		Date publishedAt = new Date();
		try {
			if (isAdmin) {
				User hasExistUser = userRepo.findByEmail(authorEmail);
				if (hasExistUser == null) {
					return false;
				}
				Post post = new Post();
				post.setAuthor(hasExistUser.getName());
				post.setContent(content);
				post.setTitle(title);
				post.setExcerpt(excerpt);
				post.setIsPublished("yes");
				post.setPublishedAt(publishedAt);
				post.setCreatedAt(createdAt);
				post.setUpdatedAt(updatedAt);
				post.setUser(hasExistUser);
				postRepo.save(post);
			} else {
				User user = userRepo.findByEmail(email);
				Post post = new Post();
				post.setAuthor(user.getName());
				post.setContent(content);
				post.setTitle(title);
				post.setExcerpt(excerpt);
				post.setIsPublished("yes");
				post.setPublishedAt(publishedAt);
				post.setCreatedAt(createdAt);
				post.setUpdatedAt(updatedAt);
				post.setUser(user);
				postRepo.save(post);
			}

			isCreated = true;
		} catch (Exception e) {
			System.out.println(e);
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
			int pageSize, String searchText) {
		Set<Post> posts = new HashSet<>();
		searchText = searchText.trim();
		List<String> authorsList = null;
		List<String> tagsList = null;
		Pageable pageble = PageRequest.of(pageNo - 1, pageSize);
		Date strtDate = null;
		Date eDate = null;
		System.out.println(searchText);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		if (startDate.length() != 0 && endDate.length() != 0) {
			try {
				startDate = startDate + " 00:00:00.000";
				endDate = endDate + " 23:59:59.999";
				strtDate = dateFormat.parse(startDate);
				eDate = dateFormat.parse(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (authors != null) {
			authorsList = new ArrayList<>();
			for (String author : authors) {
				authorsList.add(author);
			}
		}
		if (tags != null) {
			tagsList = new ArrayList<>();
			for (String tag : tags) {
				tagsList.add(tag);
			}
		}
		System.out.println(strtDate);
		if (searchText.length() == 0) {
			String searchTexts[] = null;
			posts = postDao.filterBySearch(authors, tags, startDate, endDate, pageNo, pageSize, authorsList, tagsList,
					pageble, strtDate, eDate, searchTexts);
		} else if (searchText != null) {
			String searchTexts[] = searchText.split(" ");
			posts = postDao.filterBySearch(authors, tags, startDate, endDate, pageNo, pageSize, authorsList, tagsList,
					pageble, strtDate, eDate, searchTexts);
		}
		return posts;
	}

	public Set<Post> searchPostPage(String searchText, int pageNo, int pageSize) {
		searchText = searchText.trim();
		String searchTexts[] = searchText.split(" ");
		Set<Post> searchPosts = new HashSet<>();
		Pageable pageble = PageRequest.of(pageNo - 1, pageSize);
		for (String text : searchTexts) {
			Page<Post> postsBySearch = postRepo.searchByTitleAuthorTagsContent(text, pageble);
			searchPosts.addAll(postsBySearch.getContent());
		}
		return searchPosts;
	}

	public Page<Post> sortPost(int pageNo, int pageSize, String sortType) {
		PageRequest pageable = null;
		Page<Post> sortedPost = null;
		if (sortType.equals("asc")) {
			pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.ASC, "publishedAt"));
			sortedPost = postRepo.findAllByisPublishedOrderByCreatedAtAsc("yes", pageable);
		} else {
			pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.DESC, "publishedAt"));
			sortedPost = postRepo.findAllByisPublishedOrderByCreatedAtDesc("yes", pageable);
		}

		return sortedPost;
	}

	public boolean updatePost(String content, String excerpt, String title, int postId) {
		boolean isUpdated = false;
		Date updatedTime = new Date();
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

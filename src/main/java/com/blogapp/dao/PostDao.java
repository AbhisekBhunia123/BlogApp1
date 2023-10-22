package com.blogapp.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.blogapp.entities.Post;
import com.blogapp.repositories.PostRepo;
import com.blogapp.repositories.TagRepo;

@Repository
public class PostDao {

	@Autowired
	PostRepo postRepo;

	@Autowired
	TagRepo tagRepo;

	public Set<Post> filterBySearch(String[] authors, String[] tags, String startDate, String endDate, int pageNo,
			int pageSize, List<String> authorsList, List<String> tagsList, Pageable pageble, Date strtDate, Date eDate,
			String[] searchTexts) {
		Set<Post> posts = new HashSet<>();
		if (searchTexts != null) {
			for (String searchText : searchTexts) {
				posts.addAll(postRepo.filterAndSearchPosts(authorsList, tagsList, strtDate, eDate, searchText, pageble)
						.getContent());
			}
		} else {
			posts.addAll(
					postRepo.filterAndSearchPosts(authorsList, tagsList, strtDate, eDate, null, pageble).getContent());
		}

		return posts;
	}

	public Set<Post> filterBySort() {
		Set<Post> posts = new HashSet<>();
		return posts;
	}
}

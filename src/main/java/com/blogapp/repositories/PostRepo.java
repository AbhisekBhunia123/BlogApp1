package com.blogapp.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blogapp.entities.Post;

public interface PostRepo extends JpaRepository<Post, Integer>{
	
	public List<Post> findByIsPublished(String isPublished);
	
	public List<Post> findByIsPublishedAndUserId(String isPublished,int userId);
	
	public List<Post> findByAuthor(String author);
	
	public List<Post> findByPublishedAt(String date);
	
	@Query("SELECT p FROM Post p WHERE " +
	           "LOWER(p.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
	           "OR LOWER(p.author) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
	           "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
	public Page<Post> findFromSearch(Pageable pageble,@Param("searchTerm") String search);
	
	@Query(value = "SELECT p FROM Post p ORDER BY p.created_at "
            + "DESC :sortType",
            countQuery = "SELECT COUNT(p) FROM Post p",
            nativeQuery = true)
	public Page<Post> sortPost(Pageable pageable,@Param("sortType") String sortType);
	
	Page<Post> findByIsPublished(String published, Pageable pageable);
	
}

package com.blogapp.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blogapp.entities.Post;

public interface PostRepo extends JpaRepository<Post, Integer> {

	public List<Post> findByIsPublished(String isPublished);

	public List<Post> findByIsPublishedAndUserId(String isPublished, int userId);

	public List<Post> findByAuthor(String author);

	public List<Post> findByPublishedAt(Date date);

	public Page<Post> findAllByisPublishedOrderByCreatedAtAsc(String isPublished,Pageable pageable);

	public Page<Post> findAllByisPublishedOrderByCreatedAtDesc(String isPublished,Pageable pageable);

	public Page<Post> findByIsPublished(String published, Pageable pageable);

	public Page<Post> findByAuthorInAndCreatedAtBetween(List<String> authors, Date startDate, Date endDate,
			Pageable pageable);

	public Page<Post> findByAuthorIn(List<String> authors, Pageable pageable);

	public Page<Post> findByCreatedAtBetween(Date startDate, Date endDate, Pageable pageable);
	
	
	@Query("SELECT p FROM Post p " +
		       "LEFT JOIN p.tags t " +
		       "WHERE ((:authors is null OR p.author IN :authors) AND (p.isPublished = 'yes') " +
		       "AND (:tags is null OR t.name IN :tags) " +
		       "AND (CAST(:startDate AS java.util.Date) is null OR p.createdAt >= CAST(:startDate AS java.util.Date)) " +
		       "AND (CAST(:endDate AS java.util.Date) is null OR p.createdAt <= CAST(:endDate AS java.util.Date))) " +
		       "AND ((:searchText is null OR (p.title LIKE CONCAT('%', :searchText, '%') " +
		       "OR p.content LIKE CONCAT('%', :searchText, '%') " +
		       "OR p.author LIKE CONCAT('%', :searchText, '%') " +
		       "OR t.name LIKE CONCAT('%', :searchText, '%'))))")
		Page<Post> filterAndSearchPosts(@Param("authors") List<String> authors,
		                               @Param("tags") List<String> tags,
		                               @Param("startDate") Date startDate,
		                               @Param("endDate") Date endDate,
		                               @Param("searchText") String searchText,
		                               Pageable pageable);
	
	@Query("SELECT p FROM Post p " +
		       "LEFT JOIN p.tags t " +
		       "WHERE (p.isPublished = 'yes') AND ((:searchText is null OR (p.title LIKE CONCAT('%', :searchText, '%') " +
		       "OR p.content LIKE CONCAT('%', :searchText, '%') " +
		       "OR p.author LIKE CONCAT('%', :searchText, '%') " +
		       "OR t.name LIKE CONCAT('%', :searchText, '%'))))")
		Page<Post> searchByTitleAuthorTagsContent(
		                               @Param("searchText") String searchText,
		                               Pageable pageable);

}

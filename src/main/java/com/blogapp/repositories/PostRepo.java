package com.blogapp.repositories;

import java.util.ArrayList;
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

	@Query("SELECT p FROM Post p WHERE " + "LOWER(p.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) "
			+ "OR LOWER(p.author) LIKE LOWER(CONCAT('%', :searchTerm, '%')) "
			+ "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
	public List<Post> findFromSearch(@Param("searchTerm") String search);

	@Query("SELECT p FROM Post p WHERE p.isPublished = 'yes' AND ("
			+ "LOWER(p.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) "
			+ "OR LOWER(p.author) LIKE LOWER(CONCAT('%', :searchTerm, '%')) "
			+ "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
	public Page<Post> findFromSearchPage(Pageable pageable, @Param("searchTerm") String search);

	public Page<Post> findAllByOrderByCreatedAtAsc(Pageable pageable);

	public Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

	public Page<Post> findByIsPublished(String published, Pageable pageable);

	@Query("SELECT p FROM Post p " + "WHERE (:authors IS NULL OR p.author IN :authors) "
			+ "AND (:startDate IS NULL OR p.createdAt >= :startDate) "
			+ "AND (:endDate IS NULL OR p.createdAt <= :endDate)")
	Page<Post> filterPost(@Param("authors") ArrayList<String> authors, @Param("startDate") Date startDate,
			@Param("endDate") Date endDate, Pageable pageable);

	public Page<Post> findByAuthorInAndCreatedAtBetween(List<String> authors, Date startDate, Date endDate,
			Pageable pageable);

	public Page<Post> findByAuthorIn(List<String> authors, Pageable pageable);

	public Page<Post> findByCreatedAtBetween(Date startDate, Date endDate, Pageable pageable);

	@Query("SELECT p FROM Post p " + "JOIN p.tags t " + "WHERE (:authors IS NULL OR p.author IN :authors) "
			+ "AND (:tags IS NULL OR t.name IN :tags) " + "AND (:startDate IS NULL OR p.createdAt >= :startDate) "
			+ "AND (:endDate IS NULL OR p.createdAt <= :endDate)")
	public Page<Post> filterByAuthorTagAndCreatedAt(@Param("authors") List<String> authors,
			@Param("tags") List<String> tags, @Param("startDate") Date startDate, @Param("endDate") Date endDate,
			Pageable pageable);


	@Query("SELECT p FROM Post p " + "JOIN p.tags t " + "WHERE (:authors IS NULL OR p.author IN :authors) "
			+ "AND (:tags IS NULL OR t.name IN :tags)")
	public Page<Post> filterByTagNameAndAuthor(@Param("authors") List<String> authors, @Param("tags") List<String> tags,
			Pageable pageable);

}

package com.blogapp.repositories;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.blogapp.entities.Tag;

public interface TagRepo extends JpaRepository<Tag, Integer>{

	public List<Tag> findByName(String name);
	
	@Query("SELECT t FROM Tag t WHERE t.name IN :tagNames")
    Page<Tag> findByTagNamesIn(List<String> tagNames, Pageable pageable);
	
	
}

package com.blogapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogapp.entities.Tag;

public interface TagRepo extends JpaRepository<Tag, Integer>{

	public List<Tag> findByName(String name);
	
}

package com.blogapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.blogapp.entities.User;

public interface UserRepo extends CrudRepository<User, Integer>{

	public List<User> findByEmailAndPassword(String email,String password);
	
	public User findByEmail(String name);
	
	@Query("select u from User u where u.email = :userName")
	public User getUserByUserName(String userName);
	
}

package com.blogapp.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.blogapp.entities.User;

public interface UserRepo extends CrudRepository<User, Integer>{

	public List<User> findByEmailAndPassword(String email,String password);
}

package com.blogapp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blogapp.entities.User;
import com.blogapp.repositories.UserRepo;

@Component
public class UserServices {
	@Autowired
	UserRepo userRepo;
	
	public boolean registerUser(String name,String email,String password) {
		boolean isSaved = false;
		try {
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		userRepo.save(user);
		isSaved = true;
		}catch(Exception e) {
			
		}
		
		return isSaved;
	}
	
	public User AuthenticateUser(String email,String password) {
		User user = null;
		try {
			List<User> users = userRepo.findByEmailAndPassword(email,password);
			if(users.size() != 0) {
				user = users.get(0);
			}
		}catch(Exception e) {
			
		}
		return user;
	}
	
}

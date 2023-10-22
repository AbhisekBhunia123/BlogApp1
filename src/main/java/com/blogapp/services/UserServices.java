package com.blogapp.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.blogapp.entities.User;
import com.blogapp.repositories.UserRepo;

@Service
public class UserServices {
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	BCryptPasswordEncoder bcryptPasswordEncoder;

	public boolean registerUser(String name, String email, String password) {
		boolean isSaved = false;
		try {
			User user = new User();
			user.setName(name);
			user.setEmail(email);
			user.setPassword(bcryptPasswordEncoder.encode(password));
			userRepo.save(user);
			isSaved = true;
		} catch (Exception e) {
			System.out.println(e);
		}
		return isSaved;
	}

	public User AuthenticateUser(String email, String password) {
		User user = null;
		try {
			List<User> users = userRepo.findByEmailAndPassword(email, password);
			if (users.size() != 0) {
				user = users.get(0);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return user;
	}

	public Set<String> getallAuthorName() {
		Iterable<User> iterable = userRepo.findAll();
		List<User> users = new ArrayList<>();
		Set<String> usersName = new HashSet<>();
		iterable.forEach(users::add);
		for (int index = 0; index < users.size(); index++) {
			String userName = users.get(index).getName();
			usersName.add(userName);
		}
		return usersName;
	}
	
	public User findByEmail(String email) {
		return userRepo.findByEmail(email);
	}

}

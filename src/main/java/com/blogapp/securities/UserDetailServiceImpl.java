package com.blogapp.securities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.blogapp.entities.User;
import com.blogapp.repositories.UserRepo;

public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepo userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.getUserByUserName(username);
		System.out.print(username + " ");
		if (user == null) {
			System.out.print("hello");
			throw new UsernameNotFoundException("Could not found User !");
		}
		System.out.println(user.getEmail());
		CustomUserDetails customUserDetails = new CustomUserDetails(user);

		return customUserDetails;
	}

}
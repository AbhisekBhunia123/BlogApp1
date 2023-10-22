package com.restapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blogapp.entities.User;
import com.blogapp.services.UserServices;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserServices userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestParam("name") String name,
                                              @RequestParam("email") String email,
                                              @RequestParam("password") String password) {
        if (userService.findByEmail(email) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with this email already exists");
        }
        boolean isSaved = userService.registerUser(name, email, password);
        if (isSaved) {
            return ResponseEntity.ok("User registered successfully");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user");
    }

    @PostMapping("/login")
    public ResponseEntity<User> userLogin(@RequestParam("email") String email,
                                        @RequestParam("password") String password) {
        User user = userService.AuthenticateUser(email, password);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}

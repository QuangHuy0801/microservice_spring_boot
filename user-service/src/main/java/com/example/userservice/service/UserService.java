package com.example.userservice.service;

import java.util.List;
import java.util.Optional;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.entity.User;

public interface UserService {
	void createUser(UserRequest userRequest);
	List<UserResponse> getAllUserTest();
	Optional<User> findbyid(String id);
	//main
	List<User> getAllUser();

	User saveUser(User user);

	User updateUser(User user);

	void deleteUserById(String id);
	
	User GetUserByEmail(String email);

	User findByIdAndRole(String id, String role);

	List<User> findAll();  
	List<User> findByRole(String role);
}

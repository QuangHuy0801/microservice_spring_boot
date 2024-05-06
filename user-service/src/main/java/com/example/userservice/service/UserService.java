package com.example.userservice.service;

import java.util.List;
import java.util.Optional;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.entity.User;

public interface UserService {
	void createUser(UserRequest userRequest);
	List<UserResponse> getAllUser();
	Optional<User> findbyid(String id);
}

package com.example.userservice.service.impl;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

	@Autowired
	SessionFactory factory;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public void createUser(UserRequest userRequest) {
		User user =User.builder()
				.id(userRequest.getId())
				.avatar(userRequest.getAvatar())
				.address(userRequest.getAddress())
				.email(userRequest.getEmail())
				.login_Type(userRequest.getLogin_Type())
				.password(userRequest.getPassword())
				.phone_Number(userRequest.getPhone_Number())
				.role(userRequest.getRole())
				.user_Name(userRequest.getUser_Name())
				.build();
		userRepository.save(user);
	}
	@Override
	public List<UserResponse> getAllUser() {
		List<User> users = userRepository.findAll();
		return users.stream().map(user-> mapToUserResponse(user)).toList();
	}
	private UserResponse mapToUserResponse(User user) {
		return UserResponse.builder()
				.id(user.getId())
				.avatar(user.getAvatar())
				.address(user.getAddress())
				.email(user.getEmail())
				.login_Type(user.getLogin_Type())
				.password(user.getPassword())
				.phone_Number(user.getPhone_Number())
				.role(user.getRole())
				.user_Name(user.getUser_Name())
				.build();
	}
	
	@Override
	public Optional<User> findbyid(String id) {
		return userRepository.findById(id);
	}

	
}

package com.example.userservice.controller;

import java.util.Base64;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.entity.User;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;



@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {
	@Autowired
	UserService userService;

	@Autowired
	HttpSession session;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void CreateOrder(@RequestBody UserRequest userRequest) {
		userService.createUser(userRequest);
	}
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<UserResponse> GetAllOrder() {
		return userService.getAllUser();
	}
	@RequestMapping("/findbyid")
	@Transactional(readOnly=true)
	@SneakyThrows
	public Boolean FindById(@RequestParam String id) {
		Thread.sleep(10000);
	    return userService.findbyid(id).isPresent();
	}

}

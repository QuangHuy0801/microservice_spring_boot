package com.example.orderservice.service;

import java.util.List;

import com.example.orderservice.dto.CartDto;
import com.example.orderservice.dto.UserDto;

public interface UserServiceClient {
	UserDto findByIdAndRole(String id, String role);
	List<CartDto> GetAllCartByUser_id(String user_id);
	String DeleteCart(int cart_id, String user_id);
}

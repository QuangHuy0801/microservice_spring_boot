package com.example.userservice.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class CartDto {
	private int id;
	private int count;
//	private UserDto user;
	private ProductDto product;
}

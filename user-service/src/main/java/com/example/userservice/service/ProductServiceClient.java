package com.example.userservice.service;

import com.example.userservice.dto.ProductDto;

public interface ProductServiceClient {
	 ProductDto getProductById(int id);
}

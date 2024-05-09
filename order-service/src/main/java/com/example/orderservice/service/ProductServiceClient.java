package com.example.orderservice.service;

import com.example.orderservice.dto.ProductDto;

public interface ProductServiceClient {
	 ProductDto getProductById(int id);
}

package com.example.orderservice.service;

import com.example.orderservice.dto.ProductDto;
import com.example.orderservice.dto.PromotionDto;

public interface ProductServiceClient {
	 ProductDto getProductById(int id);
	 PromotionDto getPromotionByProductID(int productId);
	 ProductDto saveProduct(ProductDto productDto);
}

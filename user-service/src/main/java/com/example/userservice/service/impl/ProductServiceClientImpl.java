package com.example.userservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.userservice.dto.ProductDto;
import com.example.userservice.service.ProductServiceClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceClientImpl implements ProductServiceClient{
	@Autowired
	WebClient.Builder webClientBuilder;
	 @Override
	    public ProductDto getProductById(int id) {
	        try {
	            ProductDto productDto = webClientBuilder.build()
	                    .get()
	                    .uri("http://product-service/api/product/getproductbyid?id={id}", id)
	                    .retrieve()
	                    .bodyToMono(ProductDto.class)
	                    .block();

	            return productDto;
	        } catch (Exception e) {
	            // Xử lý ngoại lệ nếu có
	            return null; // hoặc trả về một giá trị mặc định
	        }
	    }
	
}

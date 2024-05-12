package com.example.orderservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.orderservice.dto.ProductDto;
import com.example.orderservice.dto.PromotionDto;
import com.example.orderservice.service.ProductServiceClient;

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
	 @Override
	    public PromotionDto getPromotionByProductID(int productId) {
	        try {
	        	PromotionDto PromotionDto = webClientBuilder.build()
	                    .get()
	                    .uri("http://product-service/api/product/promotion/checkProduct/{productId}", productId)
	                    .retrieve()
	                    .bodyToMono(PromotionDto.class)
	                    .block();

	            return PromotionDto;
	        } catch (Exception e) {
	            // Xử lý ngoại lệ nếu có
	            return null; // hoặc trả về một giá trị mặc định
	        }
	    }
	 @Override
	    public ProductDto saveProduct(ProductDto productDto) {
	        try {
	        	ProductDto productDtosaved = webClientBuilder.build()
	                    .get()
	                    .uri("http://product-service/api/product/saveproduct/{productDto}", productDto)
	                    .retrieve()
	                    .bodyToMono(ProductDto.class)
	                    .block();

	            return productDtosaved;
	        } catch (Exception e) {
	            // Xử lý ngoại lệ nếu có
	            return null; // hoặc trả về một giá trị mặc định
	        }
	    }
	
}

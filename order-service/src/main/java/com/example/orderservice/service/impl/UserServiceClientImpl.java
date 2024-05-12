package com.example.orderservice.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.orderservice.dto.CartDto;
import com.example.orderservice.dto.ProductDto;
import com.example.orderservice.dto.UserDto;
import com.example.orderservice.service.UserServiceClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceClientImpl implements UserServiceClient{
	@Autowired
	WebClient.Builder webClientBuilder;
	 @Override
	    public UserDto findByIdAndRole(String id, String role){
	        try {
	        	UserDto UserDto = webClientBuilder.build()
	                    .get()
	                    .uri("http://user-service/api/user/findbyidandrole?id={id}&role={role}", id,role)
	                    .retrieve()
	                    .bodyToMono(UserDto.class)
	                    .block();

	            return UserDto;
	        } catch (Exception e) {
	            // Xử lý ngoại lệ nếu có
	            return null; // hoặc trả về một giá trị mặc định
	        }
	    }
		 @Override
		 public List<CartDto> GetAllCartByUser_id(String user_id) {
			    try {
			        Flux<CartDto> cartFlux = webClientBuilder.build()
			                .get()
			                .uri("http://user-service/api/user/getallcartbyuserid?user_id={user_id}", user_id)
			                .retrieve()
			                .bodyToFlux(CartDto.class); 

			        return cartFlux.collectList().block();
			    } catch (Exception e) {
			        return Collections.emptyList(); 
			    }
			}
		 @Override
		    public String DeleteCart(int cart_id, String user_id){
		        try {
		        	String resuls = webClientBuilder.build()
		                    .post()
		                    .uri("http://user-service/api/user/deletecart?cart_id={cart_id}&user_id={user_id}", cart_id,user_id)
		                    .retrieve()
		                    .bodyToMono(String.class)
		                    .block();

		            return resuls;
		        } catch (Exception e) {
		            // Xử lý ngoại lệ nếu có
		            return null; // hoặc trả về một giá trị mặc định
		        }
		    }
}

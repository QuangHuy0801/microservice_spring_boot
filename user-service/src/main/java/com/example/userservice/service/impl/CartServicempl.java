package com.example.userservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.userservice.entity.Cart;
import com.example.userservice.repository.CartRepository;
import com.example.userservice.service.CartService;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class CartServicempl implements CartService{

	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	WebClient.Builder webClientBuilder;
	/**
	 *
	 */
	@Override
	public void deleteById(int id) {
		// TODO Auto-generated method stub
		cartRepository.deleteById(id);
	}
	@Override
	public List<Cart> GetAllCartByUser_id(String user_id) {
		// TODO Auto-generated method stub
		return cartRepository.findAllByUser_id(user_id);
	}
	@Override
	public Cart saveCart(Cart cart) {
		  try {        
		        Boolean productExists = webClientBuilder.build().get()
	            .uri("http://product-service/api/product/findbyid",
	                    uriBuilder -> uriBuilder.queryParam("id", cart.getProduct_id()).build())
	            .retrieve()
	            .bodyToMono(Boolean.class)
	            .block();

		        if (productExists != null && productExists) {
		            return cartRepository.save(cart);
		        } else {
		            log.error("Product does not exist.");
		            return null;
		        }
		    } catch (Exception e) {
		        log.error("Error while checking product existence: {}", e.getMessage());
		        throw new IllegalArgumentException("Error occurred while checking product existence");
		    }
	}
	@Override
	public int getProductByCartId(int cart_id) {
		// TODO Auto-generated method stub
		return cartRepository.findProductByCart_id(cart_id);
	}
}

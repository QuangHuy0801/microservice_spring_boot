package com.example.userservice.service;

import java.util.List;

import com.example.userservice.entity.Cart;

public interface CartService {
	
	void deleteById(int id);
	
	List<Cart> GetAllCartByUser_id(String user_id);
	
	Cart saveCart(Cart cart);
	
	int getProductByCartId(int cart_id);
	
}

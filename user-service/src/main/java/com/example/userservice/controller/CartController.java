package com.example.userservice.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.dto.CartDto;
import com.example.userservice.dto.ProductDto;
import com.example.userservice.entity.Cart;
import com.example.userservice.entity.User;
import com.example.userservice.service.CartService;
import com.example.userservice.service.ProductServiceClient;
import com.example.userservice.service.UserService;

import lombok.RequiredArgsConstructor;

//import com.example.userservice.model.CartDto;
//import com.example.userservice.service.ProductService;
//import com.example.userservice.entity.Product;
@CrossOrigin("*")
@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class CartController {
	@Autowired
	CartService cartService;
//	@Autowired
//	ProductService productService;
	@Autowired
	UserService userService;
	@Autowired
	ProductServiceClient productServiceClient;
	
	
	@Autowired
    private ModelMapper modelMapper;

	@PostMapping(path = "/addtocart", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<Cart> addToCart(String user_id, int product_id, int count) {
		System.out.println(user_id + product_id + count);
		User user = userService.findByIdAndRole(user_id, "user");
		List<Cart> listCart = cartService.GetAllCartByUser_id(user_id);
		int flag = 0;
		Cart cart = new Cart();
		for (Cart y : listCart) {
			if (y.getProduct_id() == product_id) {
				y.setCount(y.getCount() + count);
				cartService.saveCart(y);
				cart = y;
				flag = 1;
			}
		}
		if (flag == 0) {
			Cart newCart = new Cart();
			newCart.setCount(count);
			newCart.setProduct_id(product_id);
			newCart.setUser(user);
			cart = cartService.saveCart(newCart);
		}
		return new ResponseEntity<>(cart, HttpStatus.OK);
	}

	@GetMapping(path = "/cartofuser")
    public ResponseEntity<List<CartDto>> cartOfUser(User user){
        List<Cart> listCart = cartService.GetAllCartByUser_id(user.getId());
        List<CartDto> lisCartDto = new ArrayList<>();
        System.out.println(user.getId());
        for(Cart cart : listCart) {
            CartDto cartDto = new CartDto();
            cartDto.setId(cart.getId());
            cartDto.setCount(cart.getCount());
            
            // Lấy thông tin ProductDto từ ProductServiceClient
            ProductDto productDto = productServiceClient.getProductById(cart.getProduct_id());
            if(productDto != null) {
                cartDto.setProduct(productDto);
            } else {
               
            }

            lisCartDto.add(cartDto);
        }
        return new ResponseEntity<>(lisCartDto, HttpStatus.OK);
    }
	
	@GetMapping(path = "/getallcartbyuserid")
    public ResponseEntity<List<CartDto>> GetAllCartByUser_id(String user_id){
        List<Cart> listCart = cartService.GetAllCartByUser_id(user_id);
        List<CartDto> lisCartDto = new ArrayList<>();
        for(Cart cart : listCart) {
            CartDto cartDto = new CartDto();
            cartDto.setId(cart.getId());
            cartDto.setCount(cart.getCount());
            
            // Lấy thông tin ProductDto từ ProductServiceClient
            ProductDto productDto = productServiceClient.getProductById(cart.getProduct_id());
            if(productDto != null) {
                cartDto.setProduct(productDto);
            } else {
               
            }

            lisCartDto.add(cartDto);
        }
        return new ResponseEntity<>(lisCartDto, HttpStatus.OK);
    }
	
//	@PostMapping(path = "/deletecart", consumes = "application/x-www-form-urlencoded")
	@PostMapping(path = "/deletecart")
	public ResponseEntity<String> DeleteCart(int cart_id, String user_id) {
		List<Cart> carts = cartService.GetAllCartByUser_id(user_id);
		System.out.println(cart_id + user_id +"abc");
		for(Cart y:carts) {
			if(cart_id == y.getId())
				cartService.deleteById(cart_id);
		}
		return new ResponseEntity<>("successfully", HttpStatus.OK);
	}
}

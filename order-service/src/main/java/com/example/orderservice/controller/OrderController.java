package com.example.orderservice.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.Order_Item;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.service.Order_ItemService;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
public class OrderController {
	@Autowired 
	OrderService orderService;
	
	@Autowired
	Order_ItemService order_ItemService;
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@CircuitBreaker(name="user",fallbackMethod = "fallbackMethod")
	@TimeLimiter(name="user")
	public CompletableFuture<String> CreateOrder(@RequestBody OrderRequest orderRequest) {
		 return CompletableFuture.supplyAsync(() ->orderService.createOrder(orderRequest));

	}
	public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest,RuntimeException runtimeException) {
		return CompletableFuture.supplyAsync(() -> "Oops, đã có lỗi xảy ra, vui lòng order lại sau!");
	}
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<OrderResponse> GetAllOrder() {
		return orderService.getAllOrder();
	}
}

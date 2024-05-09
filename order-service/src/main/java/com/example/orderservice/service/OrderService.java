package com.example.orderservice.service;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.entity.Order;

public interface OrderService {
	String createOrder(OrderRequest productRequest);
	
	List<OrderResponse> getAllOrder();

	public Order saveOrder(Order order);
	
	List<Order> getAllOrderByUser_Id(String id);

	Order findById(int id);

	List<Order> findAll();

	Page<Order> findAll(Pageable pageable);

	void deleteById(int id);

	List<Order> findAllByPayment_Method(String payment_Method, String user_id);

	List<Order> findAllByPayment_Method(String payment_Method);
	List<Order> filterByStatus(String status);
}

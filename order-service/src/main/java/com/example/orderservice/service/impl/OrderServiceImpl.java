package com.example.orderservice.service.impl;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService{
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	private final WebClient.Builder webClientBuilder;
	
	@Override
	public String createOrder(OrderRequest orderRequest) {
	    Order order = Order.builder()
	            .total(orderRequest.getTotal())
	            .booking_Date(orderRequest.getBooking_Date())
	            .payment_Method(orderRequest.getPayment_Method())
	            .status(orderRequest.getStatus())
	            .fullname(orderRequest.getFullname())
	            .country(orderRequest.getCountry())
	            .address(orderRequest.getAddress())
	            .phone(orderRequest.getPhone())
	            .email(orderRequest.getEmail())
	            .note(orderRequest.getNote())
	            .user_id(orderRequest.getUser_id())
	            .order_Item(orderRequest.getOrder_Item())
	            .build();

	    try {
//	        Boolean userExists = webClient.get()
//	                .uri(uriBuilder -> uriBuilder
//	                        .scheme("http")
//	                        .host("localhost")
//	                        .port(8083)
//	                        .path("/api/user/findbyid")
//	                        .queryParam("id", orderRequest.getUser_id())
//	                        .build())
//	                .retrieve()
//	                .bodyToMono(Boolean.class)
//	                .block();
	        
	        Boolean userExists = webClientBuilder.build().get()
            .uri("http://user-service/api/user/findbyid",
                    uriBuilder -> uriBuilder.queryParam("id", orderRequest.getUser_id()).build())
            .retrieve()
            .bodyToMono(Boolean.class)
            .block();

	        if (userExists != null && userExists) {
	            orderRepository.save(order);
	            log.info("Product {} is saved", order.getId());
	            return "Lưu order thành công";
	        } else {
	            log.error("User does not exist.");
	            // Handle the case where the user does not exist
	            return "Lưu order thất bại";
	        }
	    } catch (Exception e) {
	        log.error("Error while checking user existence: {}", e.getMessage());
	        throw new IllegalArgumentException("Error occurred while checking user existence");
	    }
	}

	
	@Override
	public List<OrderResponse> getAllOrder() {
		List<Order> orders = orderRepository.findAll();
		return orders.stream().map(order-> mapToOrderResponse(order)).toList();
	}
	private OrderResponse mapToOrderResponse(Order order) {
		return OrderResponse.builder()
				.id(order.getId())
				.total(order.getTotal())
				.booking_Date(order.getBooking_Date())
				.payment_Method(order.getPayment_Method())
				.status(order.getStatus())
				.fullname(order.getFullname())
				.country(order.getCountry())
				.address(order.getAddress())
				.phone(order.getPhone())
				.email(order.getEmail())
				.note(order.getNote())
				.user_id(order.getUser_id())
				.order_Item(order.getOrder_Item())
				.build();
	}
	
	
	//Main

	@Override
	public Order saveOrder(Order order) {
		return orderRepository.save(order);
	}
	@Override
	public List<Order> getAllOrderByUser_Id(String id) {
		// TODO Auto-generated method stub
		return orderRepository.findAllByUser_id(id);
	}
	@Override
	public Order findById(int id) {
		return orderRepository.findById(id);
	}
	@Override
	public List<Order> findAll() {
		return orderRepository.findAll();
	}
	@Override
	public Page<Order> findAll(Pageable pageable) {
		return orderRepository.findAll(pageable);
	}
	@Override
	public void deleteById(int id) {
		orderRepository.deleteById(id);
	}
	@Override
	public List<Order> findAllByPayment_Method(String payment_Method, String user_id) {
		return orderRepository.findAllByPayment_Method(payment_Method, user_id);
	}
	
	@Override
	public List<Order> findAllByPayment_Method(String payment_Method) {
		return orderRepository.findAllByPayment_Method(payment_Method);
	}
	@Override
	public List<Order> filterByStatus(String status) {
		return orderRepository.filterByStatus(status);
	}


}

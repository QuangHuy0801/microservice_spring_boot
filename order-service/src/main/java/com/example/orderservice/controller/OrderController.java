package com.example.orderservice.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;

import com.example.orderservice.dto.CartDto;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.dto.Order_ItemDto;
import com.example.orderservice.dto.ProductDto;
import com.example.orderservice.dto.PromotionDto;
import com.example.orderservice.dto.UserDto;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.Order_Item;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.service.Order_ItemService;
import com.example.orderservice.service.ProductServiceClient;
import com.example.orderservice.service.UserServiceClient;
@CrossOrigin("*")
@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
public class OrderController {
	@Autowired 
	OrderService orderService;
	
	@Autowired
	Order_ItemService order_ItemService;
	@Autowired
	UserServiceClient userServiceClient;
	
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
	
	//Main
	
	@Autowired
    private ModelMapper modelMapper;
	
	@Autowired
	ProductServiceClient productServiceClient;
	
	@GetMapping(path = "/checkconnect")
	public ResponseEntity<Boolean> checkConnect() {
		return new ResponseEntity<>(true, HttpStatus.OK);
	}
	
	
	@PostMapping(path = "/placeorder", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<Order> placeOrder(String user_id, String fullname, String phoneNumber, String address, String paymentMethod){
		List<CartDto> listCart = userServiceClient.GetAllCartByUser_id(user_id);
		Order newOrder = new Order();
		UserDto user = userServiceClient.findByIdAndRole(user_id, "user");
		long millis = System.currentTimeMillis();
		Date booking_date = new java.sql.Date(millis);
		int total=0;
		for(CartDto y: listCart) {
			PromotionDto promotion = productServiceClient.getPromotionByProductID(y.getProduct().getId());
			if (promotion != null) {
				int PriceDiscount = y.getProduct().getPrice() - (int)(y.getProduct().getPrice() * promotion.getDiscountPercent());
				total += y.getCount() * PriceDiscount;
			}
			else {
				total += y.getProduct().getPrice() * y.getCount();
			}
		}
		newOrder.setUser_id(user.getId());
		newOrder.setFullname(fullname);
		newOrder.setBooking_Date(booking_date);
		newOrder.setCountry("Việt Nam");
		newOrder.setEmail(user.getEmail());
		newOrder.setPayment_Method(paymentMethod);
		newOrder.setAddress(address);
		newOrder.setNote(null);
		newOrder.setPhone(phoneNumber);
		newOrder.setStatus("Pending");
		newOrder.setTotal(total);
		
		newOrder = orderService.saveOrder(newOrder);
		
		for(CartDto y:listCart) {
			if(y.getCount()>y.getProduct().getQuantity()) {
				orderService.deleteById(newOrder.getId());
				return new ResponseEntity<>(null, HttpStatus.OK);
			}
			y.getProduct().setQuantity(y.getProduct().getQuantity()-y.getCount());
			y.getProduct().setSold(y.getProduct().getSold()+y.getCount());
			productServiceClient.saveProduct(y.getProduct());
			Order_Item newOrder_Item = new Order_Item();
			newOrder_Item.setCount(y.getCount());
			newOrder_Item.setOrder(newOrder);
			newOrder_Item.setProduct_id(y.getProduct().getId());
			newOrder_Item = order_ItemService.saveOrder_Item(newOrder_Item);
			userServiceClient.DeleteCart(y.getId(),user_id);
		}
		newOrder = orderService.findById(newOrder.getId());
		return new ResponseEntity<>(newOrder, HttpStatus.OK);
	}
	
	@GetMapping(path = "/order")
	public ResponseEntity<List<OrderDto>> getOrder(String user_id) {
	    System.out.println(user_id);
	    List<Order> listOrder = orderService.getAllOrderByUser_Id(user_id);
	    List<OrderDto> listOrderDto = new ArrayList<>();
	    for (Order o : listOrder) {
	        OrderDto orderDto = modelMapper.map(o, OrderDto.class);
	        for (Order_Item orderItem : o.getOrder_Item()) {
	            ProductDto productDto = productServiceClient.getProductById(orderItem.getProduct_id());
	            if (productDto != null) {
	                Order_ItemDto orderItemDto = orderDto.getOrder_Item().stream()
	                                            .filter(dto -> dto.getId() == orderItem.getId())
	                                            .findFirst()
	                                            .orElse(null);
	                if (orderItemDto != null) {
	                    orderItemDto.setProduct(productDto);
	                } else {
	                    System.out.println("Không tìm thấy Order_ItemDto với id: " + orderItem.getId());
	                }
	            } else {
	                System.out.println("Không tìm thấy sản phẩm với id: " + orderItem.getProduct_id());
	            }
	        }
	        System.out.println(orderDto.getId());
	        listOrderDto.add(orderDto);
	    }
	    return new ResponseEntity<>(listOrderDto, HttpStatus.OK);
	}

	
	@GetMapping(path = "/ordermethod")
	public ResponseEntity<List<OrderDto>> getOrderByPaymentMethod(String user_id, String method){
		System.out.println(user_id);
		List<Order> listOrder = orderService.findAllByPayment_Method(method, user_id);
		List<OrderDto> listOrderDto = new ArrayList<>();
		for(Order o: listOrder) {
	        OrderDto orderDto = modelMapper.map(o, OrderDto.class);
	        for (Order_Item orderItem : o.getOrder_Item()) {
	            ProductDto productDto = productServiceClient.getProductById(orderItem.getProduct_id());
	            if (productDto != null) {
	                Order_ItemDto orderItemDto = orderDto.getOrder_Item().stream()
	                                            .filter(dto -> dto.getId() == orderItem.getId())
	                                            .findFirst()
	                                            .orElse(null);
	                if (orderItemDto != null) {
	                    orderItemDto.setProduct(productDto);
	                } else {
	                    System.out.println("Không tìm thấy Order_ItemDto với id: " + orderItem.getId());
	                }
	            } else {
	                System.out.println("Không tìm thấy sản phẩm với id: " + orderItem.getProduct_id());
	            }
	        }
	        System.out.println(orderDto.getId());
	        listOrderDto.add(orderDto);
		}
		for(Order o: listOrder) {
		System.out.println(o.getId());
		}
		return new ResponseEntity<>(listOrderDto, HttpStatus.OK);
	}
	   @GetMapping(path = "/allOrder")
		public ResponseEntity<List<OrderDto>> getAllOrder(){
			List<Order> listOrder = orderService.findAll();
			List<OrderDto> listOrderDto = new ArrayList<>();
			for(Order o: listOrder) {
				OrderDto orderDto = modelMapper.map(o, OrderDto.class);
				
				listOrderDto.add(orderDto);
			}
			return new ResponseEntity<>(listOrderDto, HttpStatus.OK);}
	   
	   
	   @PatchMapping(path = "/order/updateStatus/{orderId}")
		public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable("orderId") int orderId, String newStatus) {
		    Order orderToUpdate = orderService.findById(orderId);
		    if (orderToUpdate == null) {
		        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		    }
		    
		    // Cập nhật trạng thái đơn hàng
		    orderToUpdate.setStatus(newStatus);
		    Order updatedOrder = orderService.saveOrder(orderToUpdate);
		    
		    // Chuyển đổi đơn hàng đã cập nhật thành DTO để trả về cho client
		    OrderDto updatedOrderDto = modelMapper.map(updatedOrder, OrderDto.class);
		    
		    return new ResponseEntity<>(updatedOrderDto, HttpStatus.OK);
		}
	   @GetMapping(path = "/orderStatus")
		public ResponseEntity<List<OrderDto>> getOrderByStatus(String status){
			List<Order> listOrderByStatus = orderService.filterByStatus(status);
			List<OrderDto> listOrderDto = new ArrayList<>();
			for(Order o: listOrderByStatus) {
				OrderDto orderDto = modelMapper.map(o, OrderDto.class);
				for (Order_Item orderItem : o.getOrder_Item()) {
		            ProductDto productDto = productServiceClient.getProductById(orderItem.getProduct_id());
		            if (productDto != null) {
		                Order_ItemDto orderItemDto = orderDto.getOrder_Item().stream()
		                                            .filter(dto -> dto.getId() == orderItem.getId())
		                                            .findFirst()
		                                            .orElse(null);
		                if (orderItemDto != null) {
		                    orderItemDto.setProduct(productDto);
		                } else {
		                    System.out.println("Không tìm thấy Order_ItemDto với id: " + orderItem.getId());
		                }
		            } else {
		                System.out.println("Không tìm thấy sản phẩm với id: " + orderItem.getProduct_id());
		            }
		        }
				listOrderDto.add(orderDto);
			}
			return new ResponseEntity<>(listOrderDto, HttpStatus.OK);
		}
}

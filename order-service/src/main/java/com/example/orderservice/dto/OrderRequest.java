package com.example.orderservice.dto;

import java.sql.Date;
import java.util.List;

import lombok.Data;
import com.example.orderservice.entity.Order_Item;

@Data
public class OrderRequest {
	private int total;
	private Date booking_Date;
	private String payment_Method;
	private String status;
	private String fullname;
	private String country;
	private String address;
	private String phone;
	private String email;
	private String note;
	private String user_id;
	private List<Order_Item> order_Item;
}

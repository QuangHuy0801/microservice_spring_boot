package com.example.userservice.dto;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import com.example.userservice.entity.Cart;
@Data
@Builder
public class UserResponse {
	private String id;
	private String login_Type;
	private String role;
	private String password;
	private String user_Name;
	private String avatar;
	private String email;
	private String phone_Number;
	private String address;
	private List<Cart> cart;
}

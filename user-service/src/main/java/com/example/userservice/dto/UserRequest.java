package com.example.userservice.dto;

import java.sql.Date;
import java.util.List;

import lombok.Data;

@Data
public class UserRequest {
	private String id;
	private String login_Type;
	private String role;
	private String password;
	private String user_Name;
	private String avatar;
	private String email;
	private String phone_Number;
	private String address;
}

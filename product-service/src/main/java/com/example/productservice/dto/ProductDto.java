package com.example.productservice.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
@Data
public class ProductDto {
	private int id;
	private String product_Name;
	private String description;
	private int sold;
	private int is_Active;
	private int is_Selling;
	private Date created_At;
	private int price;
	private int quantity;

	private List<ProductImageDto> productImage;

}

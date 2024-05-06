package com.example.productservice.dto;

import java.sql.Date;
import java.util.List;

import com.example.productservice.entity.Category;
import com.example.productservice.entity.ProductImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ProductRequest {
	private String product_Name;
	private String description;
	private int sold;
	private int is_Active;
	private int is_Selling;
	private Date created_At;
	private int price;
	private int quantity;
	private int id_category;
	private List<ProductImage> productImage;
//	private Category category;
	
	 

}

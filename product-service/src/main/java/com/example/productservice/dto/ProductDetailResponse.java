package com.example.productservice.dto;

import java.util.List;

import com.example.productservice.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponse {
	private Product product;
    private List<Product> relatedProducts;
}

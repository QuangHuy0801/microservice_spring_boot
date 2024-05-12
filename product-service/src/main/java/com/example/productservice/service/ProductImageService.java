package com.example.productservice.service;

import com.example.productservice.entity.ProductImage;

public interface ProductImageService {

	void save(ProductImage productImage);

	void deleteById(int id);
	void deleteProductImagesByProductId(int id);

}

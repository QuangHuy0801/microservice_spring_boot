package com.example.productservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.productservice.entity.ProductImage;
import com.example.productservice.repository.ProductImageRepository;
import com.example.productservice.service.ProductImageService;

@Service
public class ProductImageServiceImpl implements ProductImageService{
	@Autowired
	ProductImageRepository productImageRepository;

	@Override
	public void save(ProductImage productImage) {
		productImageRepository.save(productImage);
	}

	@Override
	public void deleteById(int id) {
		productImageRepository.deleteById(id);
	}
	@Override
	public void deleteProductImagesByProductId(int id) {
		productImageRepository.deleteProductImagesByProductId(id);
	}
	
	
}

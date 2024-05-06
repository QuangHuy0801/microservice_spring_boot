package com.example.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.productservice.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage,Integer>{

	void deleteById(int id);

}

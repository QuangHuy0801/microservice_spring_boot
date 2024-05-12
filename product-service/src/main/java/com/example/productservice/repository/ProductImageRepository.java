package com.example.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.productservice.entity.ProductImage;

import jakarta.transaction.Transactional;

public interface ProductImageRepository extends JpaRepository<ProductImage,Integer>{

	void deleteById(int id);
	@Modifying
	@Transactional
	@Query("DELETE FROM ProductImage pi WHERE pi.product.id =?1")
	void deleteProductImagesByProductId(int productId);

}

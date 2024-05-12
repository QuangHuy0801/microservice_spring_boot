package com.example.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.productservice.entity.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion,Integer>{
	Promotion getById(int id);
	
	@Query(value = "select p.* from promotion p inner join promotion_item i on p.id = i.promotion_id and p.status = 'Active' and current_date() between p.start_date and p.end_date where i.product_id = ?1", nativeQuery = true)
	Promotion getPromotionByProductId(int productId);
	
}

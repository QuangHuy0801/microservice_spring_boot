package com.example.productservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.productservice.entity.Promotion_Item;

import jakarta.transaction.Transactional;

public interface Promotion_ItemRepository extends JpaRepository<Promotion_Item,Integer>{
	void deleteById(int id);
	
	Promotion_Item getById(int id);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM Promotion_Item p WHERE p.promotion.id = ?1")
	void deleteByPromotionId(int id);
}

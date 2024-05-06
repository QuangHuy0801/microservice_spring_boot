package com.example.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.productservice.entity.Category;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
	
	Category getById(int id);

}

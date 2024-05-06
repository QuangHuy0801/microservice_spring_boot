package com.example.productservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.productservice.entity.Category;
import com.example.productservice.entity.Product;
import com.example.productservice.service.CategoryService;

@RestController
public class CategoryController {
//	@Autowired
//	CategoryService categoryService;
//
//	@GetMapping(path = "/category")
//	public ResponseEntity<List<Category>> GetCategory(){
//		List<Category> listcaCategories = categoryService.findAll();
//		return new ResponseEntity<>(listcaCategories, HttpStatus.OK);
//	}
	
}

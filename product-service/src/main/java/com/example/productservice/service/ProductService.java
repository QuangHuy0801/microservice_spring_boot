package com.example.productservice.service;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.productservice.dto.ProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.entity.Product;

public interface ProductService {	
//	Product saveProduct(Product product);
	List<Product> getAllProduct();
//
	Product getProductById(int id);
	
	void createProduct(ProductRequest productRequest);
	
	List<ProductResponse> getAllProductTest();

	
	Product saveProduct(Product product);


	Product updateProduct(Product product);

	void deleteProductById(int id);
	
	List<Product> findByProduct_NameContaining(String name);
	
	List<Product> findTop12ProductBestSellers();
	
	List<Product> findTop12ProductNewArrivals();

	Page<Product> findAll(Pageable pageable);

	Page<Product> findByProduct_NameContaining(String name, Pageable pageable);

	Page<Product> findByProduct_NameAndCategory_idContaining(String name, int category_id, Pageable pageable);

	List<Product> findTop4ProductByCategory_id(int name);
	List<Object[]> findProductStatistic();
	List<Object[]> findUnitOfProductStatistic();
	
	List<Product> getProductNotInPromotion();

	
}

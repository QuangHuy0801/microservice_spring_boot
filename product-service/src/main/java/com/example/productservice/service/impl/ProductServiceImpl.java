package com.example.productservice.service.impl;
import java.util.List;
import java.util.Optional;
import java.sql.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.productservice.dto.ProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.entity.Category;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.CategoryRepository;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepository;
	@Autowired
	CategoryRepository categoryRepository;
	
	@Override
	public void createProduct(ProductRequest productRequest) {
		Optional<Category> categoryOptional = categoryRepository.findById(productRequest.getId_category());
		if (categoryOptional.isPresent()) {
		Product product =Product.builder()
				.product_Name(productRequest.getProduct_Name())
				.description(productRequest.getDescription())
				.sold(productRequest.getSold())
				.is_Active(productRequest.getIs_Active())
				.is_Selling(productRequest.getIs_Selling())
				.created_At(productRequest.getCreated_At())
				.price(productRequest.getPrice())
				.quantity(productRequest.getQuantity())
				.category(categoryRepository.getById(productRequest.getId_category()))
				.build();
		productRepository.save(product);
		log.info("Product {} is save",product.getId());
		} else {
			log.error("Category with id {} does not exist", productRequest.getId_category());
		}
	}
	
	@Override
	public List<ProductResponse> getAllProductTest() {
		List<Product> products = productRepository.findAll();
		return products.stream().map(product-> mapToProductResponse(product)).toList();
	}
	private ProductResponse mapToProductResponse(Product product) {
		return ProductResponse.builder()
				.id(product.getId())
				.product_Name(product.getProduct_Name())
				.description(product.getDescription())
				.sold(product.getSold())
				.is_Active(product.getIs_Active())
				.is_Selling(product.getIs_Selling())
				.created_At(product.getCreated_At())
				.price(product.getPrice())
				.quantity(product.getQuantity())
				.category(product.getCategory())
				.build();
	}
	
	//main
	@Override
	public List<Product> getAllProduct() {
		// TODO Auto-generated method stub
		return productRepository.findAll();
	}
	
	@Override
	public Product saveProduct(Product product) {
		// TODO Auto-generated method stub
		return productRepository.save(product);
	}
	
	@Override
	public Product getProductById(int id) {
		// TODO Auto-generated method stub
		return productRepository.findById(id);
	}
	
	@Override
	public Product updateProduct(Product product) {
		// TODO Auto-generated method stub
		return productRepository.save(product);
	}

	@Override
	public void deleteProductById(int id) {
		// TODO Auto-generated method stub
		productRepository.deleteById( id);
	}

	@Override
	public List<Product> findByProduct_NameContaining(String name) {
		// TODO Auto-generated method stub
		return productRepository.findByProduct_NameContaining(name);
	}

	@Override
	public List<Product> findTop12ProductBestSellers() {
		// TODO Auto-generated method stub
		return productRepository.findTop12ProductBestSellers();
	}

	@Override
	public List<Product> findTop12ProductNewArrivals() {
		// TODO Auto-generated method stub
		return productRepository.findTop12ProductNewArrivals();
	}

	@Override
	public Page<Product> findAll(Pageable pageable) {
		return productRepository.findAll(pageable);
	}

	@Override
	public Page<Product> findByProduct_NameAndCategory_idContaining(String name, int category_id, Pageable pageable) {
		return productRepository.findByProduct_NameAndCategory_idContaining(name, category_id, pageable);
	}

	@Override
	public Page<Product> findByProduct_NameContaining(String name, Pageable pageable) {
		return productRepository.findByProduct_NameContaining(name, pageable);
	}

	@Override
	public List<Product> findTop4ProductByCategory_id(int id) {
		return productRepository.findTop4ProductByCategory_id(id);
	}
	@Override
	public 	List<Object[]> findProductStatistic(){
		return productRepository.findProductStatistic();
	}
	@Override
	public 	List<Object[]> findUnitOfProductStatistic(){
		return productRepository.findUnitOfProductStatistic();
	}

	@Override
	public List<Product> getProductNotInPromotion() {
		return productRepository.getProductNotInPromotion();
	}
	
}
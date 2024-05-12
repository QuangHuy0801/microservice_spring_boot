package com.example.productservice.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.productservice.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer>{
	
	
	@Query(value="select * from product p where p.product_name like %?1%",nativeQuery = true)
	List<Product> findByProduct_NameContaining(String name);
	
	@Query(value="Select * From product p ORDER BY p.quantity DESC LIMIT 12;",nativeQuery = true)
	List<Product> findTop12ProductBestSellers();
	
	@Query(value="Select * From product p ORDER BY p.created_at DESC LIMIT 12;",nativeQuery = true)
	List<Product> findTop12ProductNewArrivals();
	
	Page<Product> findAllByCategory_id(int id, Pageable pageable);
	
	Product findById(int id);
	
	@Query(value="select * from `fashionstore`.product where `fashionstore`.product.product_name like %?1% and `fashionstore`.product.category_id= ?2",nativeQuery = true)
	Page<Product> findByProduct_NameAndCategory_idContaining(String name, int category_id, Pageable pageable);
	
	@Query(value="select * from `fashionstore`.product where `fashionstore`.product.product_name like %?1%",nativeQuery = true)
	Page<Product> findByProduct_NameContaining(String name, Pageable pageable);
	
	@Query(value="select * from product p where p.category_id = ?1 ORDER BY p.sold DESC LIMIT 4;",nativeQuery = true)
	List<Product> findTop4ProductByCategory_id(int id);
		
	@Query(value= "SELECT c.category_name, SUM(p.quantity) AS total_revenue FROM product p INNER JOIN category c ON p.category_id = c.id GROUP BY c.category_name", nativeQuery = true)
	List<Object[]> findUnitOfProductStatistic();
	
	@Query(value= "SELECT c.category_name, CAST(COUNT(p.id) AS DECIMAL) AS total_revenue FROM product p INNER JOIN category c ON p.category_id = c.id GROUP BY c.category_name", nativeQuery = true)
	List<Object[]> findProductStatistic();
	
	@Query(value = "select * from product p where p.id not in (select i.product_id from promotion pro inner join promotion_item i on pro.id = i.promotion_id and pro.status = 'Active' and current_date() between pro.start_date and pro.end_date)", nativeQuery = true)
	List<Product> getProductNotInPromotion();
	
}

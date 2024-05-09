package com.example.orderservice.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.orderservice.entity.Order;

public interface OrderRepository extends JpaRepository<Order,Integer>{	
	@Query(value="select * from `order` o where o.user_id = ?1",nativeQuery = true)
	List<Order> findAllByUser_id( String user_id);

	Order findById(int id);
	
	Page<Order> findAll(Pageable pageable);

	void deleteById(int id);
	
	
	@Query(value="select * from `order` o where o.payment_method = ?1 and o.user_id = ?2",nativeQuery = true)
	List<Order> findAllByPayment_Method(String payment_Method, String user_id);
	
	@Query(value="select * from `order` o where o.payment_method = ?1",nativeQuery = true)
	List<Order> findAllByPayment_Method(String payment_Method);
	@Query(value = "select * from `order` o where o.status = ?1",nativeQuery = true)
	List<Order> filterByStatus(String status);
	
}

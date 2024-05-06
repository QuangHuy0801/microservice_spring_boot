package com.example.userservice.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.userservice.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User,String>{
	User findByEmail(String email);
	Optional<User> findById(String id);

	User findByIdAndRole(String id, String role);
	
	void deleteById(String id);
	
	@Query(value= "SELECT CASE  WHEN age < 18 THEN 'Under 18'  WHEN age BETWEEN 18 AND 30 THEN '18 - 30'  ELSE 'Over 30'  END AS age_group,  CAST(COUNT(u.age) AS DECIMAL)  as count_age FROM  user u GROUP BY age_group;", nativeQuery = true)
	List<Object[]> findAgeGroup();
	@Query(value= "SELECT c.category_name, SUM(CASE WHEN u.age < 18 THEN o.total ELSE 0 END) AS revenue_under_18,SUM(CASE WHEN u.age BETWEEN 18 AND 30 THEN o.total ELSE 0 END) AS revenue_18_to_30,SUM(CASE WHEN u.age > 30 THEN o.total ELSE 0 END) AS revenue_over_30 FROM product p INNER JOIN category c ON p.category_id = c.id INNER JOIN order_item oi ON p.id = oi.product_id INNER JOIN fashionstore.order o ON oi.order_id = o.id INNER JOIN user u ON o.user_id = u.id GROUP BY c.category_name;", nativeQuery = true)
	List<Object[]> findAgeGroupRevenue();
	
	
}

package com.example.orderservice.entity;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Set;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Data // lombok giúp generate các hàm constructor, get, set v.v.
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "`order`")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "total")
	private int total;
	
	@Column(name = "booking_Date")
	private Date booking_Date;
	
	@Column(name = "payment_Method", columnDefinition = "nvarchar(1111)")
	private String payment_Method;
	
	@Column(name = "status", columnDefinition = "nvarchar(1111)")
	private String status;
	
	@Column(name = "fullname", columnDefinition = "nvarchar(1111)")
	private String fullname;
	
	@Column(name = "country", columnDefinition = "nvarchar(1111)")
	private String country;
	
	@Column(name = "address", columnDefinition = "nvarchar(1111)")
	private String address;
	
	@Column(name = "phone", columnDefinition = "nvarchar(1111)")
	private String phone;
	
	@Column(name = "email", columnDefinition = "nvarchar(1111)")
	private String email;
	
	@Column(name = "note", columnDefinition = "nvarchar(1111)")
	private String note;
	@Column(name = "user_id", columnDefinition = "nvarchar(255)")
	private String user_id;
	
	
	
	@OneToMany(mappedBy = "order")
	private List<Order_Item> order_Item;
}

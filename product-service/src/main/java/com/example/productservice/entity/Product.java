package com.example.productservice.entity;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "product")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "product_name", columnDefinition = "nvarchar(1111)")
	private String product_Name;

	@Column(name = "description", columnDefinition = "nvarchar(11111)")
	private String description;

	@Column(name = "sold")
	private int sold;

	@Column(name = "is_active")
	private int is_Active;

	@Column(name = "is_Selling")
	private int is_Selling;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy MM dd HH:mm:ss")
	@Column(name = "created_at")
	private Date created_At;

	@Column(name = "price")
	private int price;

	@Column(name = "quantity")
	private int quantity;

	@ManyToOne
	@JoinColumn(name = "category_id")
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@JsonIgnore
	private Category category;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<ProductImage> productImage;
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<Promotion_Item> promotion_Item;

}

package com.example.productservice.entity;



import java.sql.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "promotion")
public class Promotion {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", columnDefinition = "nvarchar(1111)")
    private String name;

    @Column(name = "description", columnDefinition = "nvarchar(1111)")
    private String description;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "discount_percent")
    private double discountPercent;

    @Column(name = "status", columnDefinition = "nvarchar(1111)")
    private String status;

    @OneToMany(mappedBy = "promotion")
    private List<Promotion_Item> promotion_Item;
}


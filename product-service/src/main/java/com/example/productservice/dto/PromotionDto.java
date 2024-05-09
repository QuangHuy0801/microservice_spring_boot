package com.example.productservice.dto;

import java.sql.Date;
import java.util.List;

import lombok.Data;

@Data
public class PromotionDto {
	private int id;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private double discountPercent;
    private String status;
    private List<Promotion_ItemDto> promotion_Item;
}

package com.example.productservice.service;

import com.example.productservice.entity.Promotion_Item;

public interface Promotion_ItemService {
	Promotion_Item savePromotion_Item(Promotion_Item promotion_Item);
	
	void deleteById(int id);
	
	void deleteByPromotionId(int id);
	
	Promotion_Item getPromotionItemById(int id);
}

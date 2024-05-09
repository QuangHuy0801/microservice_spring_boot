package com.example.productservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.productservice.entity.Category;
import com.example.productservice.entity.Product;
import com.example.productservice.entity.Promotion;
import com.example.productservice.repository.PromotionRepository;
import com.example.productservice.service.PromotionService;

@Service
public class PromotionServiceImpl implements PromotionService{
	@Autowired
	PromotionRepository promotionRepository;
	
	@Override
	public Promotion savePromotion(Promotion promotion) {
		return promotionRepository.save(promotion);
	}

	@Override
	public Promotion getPromotionById(int id) {
		// TODO Auto-generated method stub
		return promotionRepository.getById(id);
	}
	
	@Override
	public void deletePromotionById(int id) {
		promotionRepository.deleteById(id);
	}

	@Override
	public List<Promotion> findAll() {
		return promotionRepository.findAll();
	}

	@Override
	public Promotion getPromotionByProductId(int productId) {
		return promotionRepository.getPromotionByProductId(productId);
	}
}

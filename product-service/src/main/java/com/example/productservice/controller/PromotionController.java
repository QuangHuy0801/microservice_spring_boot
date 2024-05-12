package com.example.productservice.controller;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.productservice.dto.PromotionDto;
import com.example.productservice.entity.Product;
import com.example.productservice.entity.Promotion;
import com.example.productservice.entity.Promotion_Item;
import com.example.productservice.service.ProductService;
import com.example.productservice.service.PromotionService;
import com.example.productservice.service.Promotion_ItemService;
import lombok.Delegate;

@RestController
@RequestMapping("api/product")
public class PromotionController {
	@Autowired
	PromotionService promotionService;

	@Autowired
	ProductService productService;

	@Autowired
	Promotion_ItemService promotion_ItemService;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping(path = "/promotion")
	public ResponseEntity<List<PromotionDto>> getAllPromotion() {
		List<Promotion> listPromo = promotionService.findAll();
		List<PromotionDto> listPromoDto = new ArrayList<>();
		for (Promotion p : listPromo) {
			PromotionDto promotionDto = modelMapper.map(p, PromotionDto.class);
			listPromoDto.add(promotionDto);
		}
		return new ResponseEntity<>(listPromoDto, HttpStatus.OK);
	}

	@PostMapping(path = "/promotion/new", consumes = "multipart/form-data")
	public ResponseEntity<Promotion> newPromotion(String name, String description, String start, String end,
			double discount, String status) throws ParseException {
		Promotion newPromotion = new Promotion();
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date startDate = new Date(dateFormat.parse(start).getTime());
			Date endDate = new Date(dateFormat.parse(end).getTime());
			newPromotion.setStartDate(startDate);
			newPromotion.setEndDate(endDate);

		} catch (ParseException e) {
			e.printStackTrace();

			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		newPromotion.setName(name);
		newPromotion.setDescription(description);
		newPromotion.setDiscountPercent(discount);
		newPromotion.setStatus(status);

		Promotion savedPromotion = promotionService.savePromotion(newPromotion);
		return new ResponseEntity<>(savedPromotion, HttpStatus.OK);
	}

	@PutMapping(path = "/promotion/update")
	public ResponseEntity<Promotion> updatePromotion(String id, String name, String description,
			String start, String end, double discount, String status) throws ParseException{
		Promotion promotion = promotionService.getPromotionById(Integer.parseInt(id));
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date startDate = new Date(dateFormat.parse(start).getTime());
			Date endDate = new Date(dateFormat.parse(end).getTime());
			promotion.setStartDate(startDate);
			promotion.setEndDate(endDate);

		} catch (ParseException e) {
			e.printStackTrace();

			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		promotion.setName(name);
		promotion.setDescription(description);
		promotion.setDiscountPercent(discount);
		promotion.setStatus(status);
		
		promotionService.savePromotion(promotion);
		return new ResponseEntity<>(promotion, HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/promotion/delete/{id}")
	public ResponseEntity<Object> deletePromotion(@PathVariable Integer id){
		Promotion promotion = promotionService.getPromotionById(id);
		if (promotion != null) {
			promotion_ItemService.deleteByPromotionId(id);
	    	promotionService.deletePromotionById(id);
	        Map<String, String> response = new HashMap<>();
	        response.put("message", "Promotion with ID " + id + " has been deleted");
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    } else {
	        Map<String, String> response = new HashMap<>();
	        response.put("error", "Promotion with ID " + id + " not found");
	        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	    }
	}

	@GetMapping(path = "/promotion/checkProduct/{productId}")
	public ResponseEntity<PromotionDto> getPromotionByProductID(@PathVariable("productId") int productId) {
		Promotion promotion = promotionService.getPromotionByProductId(productId);

		if (promotion != null) {
			PromotionDto promotionDto = modelMapper.map(promotion, PromotionDto.class);
			return new ResponseEntity<>(promotionDto, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	
	@DeleteMapping(path = "/promotion/deleteProduct/{id}")
	public ResponseEntity<Object> deleteProductInPromotion(@PathVariable Integer id){
		Promotion_Item item = promotion_ItemService.getPromotionItemById(id);
		if (item != null) {
			promotion_ItemService.deleteById(id);
	        Map<String, String> response = new HashMap<>();
	        response.put("message", "Promotion Item with ID " + id + " has been deleted");
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    } else {
	        Map<String, String> response = new HashMap<>();
	        response.put("error", "Promotion Item with ID " + id + " not found");
	        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	    } 
	}
	
	@PutMapping(path = "/promotion/addProduct")
	public ResponseEntity<PromotionDto> addProductToPromotion(@RequestParam int promotionId, @RequestBody List<Product> products){
		System.out.println(1234);
		Promotion promotion = promotionService.getPromotionById(promotionId);
		for(Product product: products) {
			Promotion_Item newItem = new Promotion_Item();
			newItem.setPromotion(promotion);
			newItem.setProduct(product);
			promotion_ItemService.savePromotion_Item(newItem);
		}
		
		Promotion savePromotion = promotionService.savePromotion(promotion);
		PromotionDto dto = modelMapper.map(savePromotion, PromotionDto.class);
		if(dto != null) {
			return new ResponseEntity<>(dto, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
		}
	}
}

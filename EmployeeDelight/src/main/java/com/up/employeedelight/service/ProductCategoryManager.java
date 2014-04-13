package com.up.employeedelight.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.up.employeedelight.domain.ProductCategory;
import com.up.employeedelight.repository.ProductCategoryRepository;

@Service
public class ProductCategoryManager {

	@Autowired
	private ProductCategoryRepository productCategoryRepo;

	public List<ProductCategory> getAll() {
		return Lists.newArrayList(productCategoryRepo.findAll());
	}
	
	public int getIdByName(String name){
		int rez = 0 ;
		for (ProductCategory pc : productCategoryRepo.findAll()) {
			if (pc.equals(name)) {
				rez = pc.getId();
				break;
			}
		}
		return rez;
	}

}

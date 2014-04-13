package com.up.employeedelight.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.up.employeedelight.domain.Product;
import com.up.employeedelight.domain.User;
import com.up.employeedelight.exceptions.EDIllegalOperation;
import com.up.employeedelight.repository.ProductRepository;

@Service
public class ProductManager {
	@Autowired
	private ProductRepository productRepo;

	
	@Autowired
	ProductCategoryManager groupMan;
	/**
	 * Returns a map with entries of the form
	 * "categoryName : products from category"
	 */
	public Map<String, List<Product>> getProducts() {
		Map<String, List<Product>> result = new HashMap<String, List<Product>>();

		String categoryName;
		Iterable<Product> products = productRepo.findAll();
		for (Product product : products) {
			categoryName = product.getProductCategory().getName();

			if (!result.keySet().contains(categoryName)) {
				result.put(categoryName, new ArrayList<Product>());
			}
			result.get(categoryName).add(product);
		}

		return result;
	}

	public void addProduct(Product prod) throws EDIllegalOperation {
		if (prod != null && !prod.getName().equals("") ) {
			for (Product p : getProductsAsList()) {
				if (p.getName().equals(prod.getName())) {
					throw new EDIllegalOperation("Prod allready exists!");
				}
			}
			
			int catId = groupMan.getIdByName(prod.getProductCategory().getName());
			prod.getProductCategory().setId(catId);
			productRepo.save(prod);
		}
			
	}
	
	public void removeProd(int id) throws EDIllegalOperation {
		for (Product p : getProductsAsList()) {
			if (p.getId() == id) {
				productRepo.delete(id);
			}
		}
		
	}

	public void updateProd(Product prod) {
		for (Product p : getProductsAsList()) {
			if (p.getId() == prod.getId()) {
				if (!prod.getName().equals("")) {
					p.setName(prod.getName());
				}
				if (!prod.getProductCategory().getName().equals("")) {
					p.getProductCategory().setName(prod.getProductCategory().getName());
					productRepo.save(prod);
				}
				
			}
		}
		
	}

	public List<Product>  getProductsAsList() {
		return Lists.newArrayList(productRepo.findAll());
	}
}

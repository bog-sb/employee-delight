package com.up.employeedelight.repository;

import org.springframework.data.repository.CrudRepository;

import com.up.employeedelight.domain.Product;
import com.up.employeedelight.domain.ProductCategory;

public interface ProductRepository extends CrudRepository<Product, Integer>{
	public void findByProductCategory(ProductCategory category);
}

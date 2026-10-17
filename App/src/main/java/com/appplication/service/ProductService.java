package com.appplication.service;


import java.util.List;

import org.jspecify.annotations.Nullable;

import com.appplication.entity.Products;
import com.appplication.dto.ProductRequestDto;

public interface ProductService {
        
	String addProduct(ProductRequestDto product);
	Products viewProduct(Long id);
	String updateProduct(Long id, ProductRequestDto retrieveProducts);
	boolean deleteProduct(Long id);
	List<Products> viewAllProducts();
	 
}

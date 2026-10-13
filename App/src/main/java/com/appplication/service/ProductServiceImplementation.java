package com.appplication.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.appplication.entity.Products;
import com.appplication.repository.ProductRepository;
import com.appplication.dto.ProductRequestDto;

@Service
public class ProductServiceImplementation implements ProductService
                 {
          
	ProductRepository productRepository;

	public ProductServiceImplementation(ProductRepository productRepository) {
		super();
		this.productRepository = productRepository;
	}
	
	 

	 
	public String addProduct(ProductRequestDto product) {
		Products p = new Products();
 		p.setName(product.getName());
		p.setPrice(product.getPrice());
		p.setDescription(product.getDescription());
		p.setPhotoUrl(product.getPhotoUrl());
		productRepository.save(p);
		return "Product Added";
	}

	 
	@Override
	public Products viewProduct(Long prodId) {
	    return productRepository.findById(prodId)
	               .orElseThrow(() -> new RuntimeException("Product not found with id: " + prodId));
	}

	 
	 
	public String updateProduct(Long id, ProductRequestDto retrieveProducts) {
	    Optional<Products> optionalProduct = productRepository.findById(id);
	    
	    if (optionalProduct.isPresent()) {
	        Products existingProduct = optionalProduct.get(); // 1. Unwrap the entity
	        
	        // 2. Update fields (do NOT alter existingProduct.setId)
	        existingProduct.setName(retrieveProducts.getName());
	        existingProduct.setPrice(retrieveProducts.getPrice());
	        existingProduct.setDescription(retrieveProducts.getDescription());
	        existingProduct.setPhotoUrl(retrieveProducts.getPhotoUrl());
	        
	        // 3. Save the unwrapped entity
	        productRepository.save(existingProduct);
	        return "Product updated successfully";
	    }
	    
	    return "Product not found with id: " + id;
	}
	

	 
	public boolean deleteProduct(Long id) {
		  if (productRepository.existsById(id)) {
			  productRepository.deleteById(id);
			  return true;
		  }
		  if (!productRepository.existsById(id)) {
			  throw new RuntimeException("Product not found" + id);
		  }
		return false;
	}

 
	  
	public List<Products> viewAllProducts() {
	return	productRepository.findAll();
	}

	
}


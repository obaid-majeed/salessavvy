 package com.appplication.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.appplication.dto.ProductRequestDto;
import com.appplication.entity.Products;
import com.appplication.repository.ProductRepository;

@Service
@Transactional
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImplementation(
            ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public String addProduct(ProductRequestDto request) {
        Products product = new Products(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getStock(),
                request.getCategory(),
                request.getImageUrl()
        );

        productRepository.save(product);
        return "Product added successfully";
    }

    @Override
    @Transactional(readOnly = true)
    public Products viewProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Product not found: " + id));
    }

    @Override
    public String updateProduct(Long id, ProductRequestDto request) {
        Products product = productRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Product not found: " + id));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());

        productRepository.save(product);
        return "Product updated successfully";
    }

    @Override
    public boolean deleteProduct(Long id) {
        Products product = productRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Product not found: " + id));

        productRepository.delete(product);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Products> viewAllProducts() {
        return productRepository.findAll();
    }
}
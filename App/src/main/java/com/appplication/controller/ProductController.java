package com.appplication.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.appplication.dto.ProductRequestDto;
import com.appplication.entity.Products;
import com.appplication.service.ProductServiceImplementation;

@CrossOrigin(origins = "*")
@RestController
public class ProductController {

    private final ProductServiceImplementation productServiceImplementation;

    public ProductController(ProductServiceImplementation productServiceImplementation) {
        this.productServiceImplementation = productServiceImplementation;
    }

    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(@RequestBody ProductRequestDto product) {
        try {
            String saveProducts = productServiceImplementation.addProduct(product);
            return ResponseEntity.ok(Map.of("message", "product added", "Product", saveProducts));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/viewProduct/{id}")
    public Products viewProduct(@PathVariable Long id) {
        return productServiceImplementation.viewProduct(id);
    }

    @PutMapping("/updateProduct/{id}")
    public String updateProduct(@PathVariable Long id, @RequestBody ProductRequestDto retrieveProducts) {
        return productServiceImplementation.updateProduct(id, retrieveProducts);
    }

    @DeleteMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productServiceImplementation.deleteProduct(id);
        return "DELETED successfully";
    }

    // FIXED: Maps BOTH /viewAllProducts and /products so the frontend always hits this method
    @GetMapping({"/viewAllProducts", "/products"})
    public List<Products> viewAllProducts() {
        return productServiceImplementation.viewAllProducts();
    }
}
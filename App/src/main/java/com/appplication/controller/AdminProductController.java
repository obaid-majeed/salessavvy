package com.appplication.controller;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.appplication.dto.ProductRequestDto;
import com.appplication.entity.Products;
import com.appplication.service.ProductService;

@RestController
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addProduct(
            @Valid @RequestBody ProductRequestDto request) {

        String message = productService.addProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", message));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDto request) {

        String message = productService.updateProduct(id, request);

        return ResponseEntity.ok(
                Map.of("message", message));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteProduct(
            @PathVariable Long id) {

        boolean deleted = productService.deleteProduct(id);

        return ResponseEntity.ok(
                Map.of("deleted", deleted));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Products> getProductById(
            @PathVariable Long id) {

        Products product = productService.viewProduct(id);

        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<List<Products>> getAllProducts() {

        List<Products> products = productService.viewAllProducts();

        return ResponseEntity.ok(products);
    }
}

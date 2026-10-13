package com.appplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.appplication.entity.Products;

public interface ProductRepository extends JpaRepository<Products,Long> {
             
}

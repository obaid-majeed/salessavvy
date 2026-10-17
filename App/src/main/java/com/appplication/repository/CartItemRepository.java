package com.appplication.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.appplication.entity.CartItem;
import com.appplication.entity.Products;
import com.appplication.entity.Users;

 

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUser(Users user);

    Optional<CartItem> findByUserAndProduct(Users user, Products product);

    Optional<CartItem> findByIdAndUser(Long id, Users user);
    
    void deleteByUser(Users user);
}
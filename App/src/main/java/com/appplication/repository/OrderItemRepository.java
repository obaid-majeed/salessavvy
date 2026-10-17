package com.appplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.appplication.entity.OrderItem;

 

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
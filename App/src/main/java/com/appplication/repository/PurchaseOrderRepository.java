package com.appplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.appplication.entity.PurchaseOrder;
import com.appplication.entity.Users;

public interface PurchaseOrderRepository
        extends JpaRepository<PurchaseOrder, Long> {

    Optional<PurchaseOrder> findByIdAndUser(Long id, Users user);
}
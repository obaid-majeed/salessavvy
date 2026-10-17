package com.appplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.appplication.entity.PaymentTransaction;

 
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
}
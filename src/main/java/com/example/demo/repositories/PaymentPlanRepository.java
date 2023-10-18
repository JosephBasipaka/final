package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Customer;
import com.example.demo.entity.PaymentPlan;

public interface PaymentPlanRepository extends JpaRepository<PaymentPlan, Long> {
    PaymentPlan findByCustomer(Customer customer);
}

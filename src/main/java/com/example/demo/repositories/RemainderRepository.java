package com.example.demo.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Reminder;

public interface RemainderRepository extends JpaRepository<Reminder, Long> {
    
    @Query("SELECT COUNT(r) FROM Reminder r WHERE r.customer = :customer")
    long countRemindersSentForCustomer(@Param("customer") Customer customer);
}
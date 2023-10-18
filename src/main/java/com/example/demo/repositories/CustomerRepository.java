package com.example.demo.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT DISTINCT c FROM Customer c JOIN c.invoices i WHERE i.dueDate < CURRENT_DATE AND i.paid = false")
    List<Customer> findCustomersWithOverdueInvoices();

    Customer findByNameAndEmail(String name,String email);
    
}


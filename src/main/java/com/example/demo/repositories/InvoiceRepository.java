package com.example.demo.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Invoice findByCustomerIdAndPaid(Long customerId, boolean paid);
}

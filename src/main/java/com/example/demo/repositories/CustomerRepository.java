package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT DISTINCT c FROM Customer c JOIN c.services s JOIN c.invoices i WHERE s.status = 'Active' AND i.dueDate < CURRENT_DATE AND i.paid = false")
    List<Customer> findCustomersWithOverdueInvoices();

    Customer findByNameAndEmail(String name, String email);

    @Query("SELECT c FROM Customer c JOIN c.services s WHERE c.name = :customerName AND s.serviceName = :serviceName")
    Customer findByCustomerNameAndServiceName(
            @Param("customerName") String customerName,
            @Param("serviceName") String serviceName);

}

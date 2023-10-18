package com.example.demo.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Service;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    Service findByCustomer(Customer customer);
    Service findByServiceName(String serviceNameString);
    Service findByCustomerIdAndServiceName(Long customerId, String serviceName);
}
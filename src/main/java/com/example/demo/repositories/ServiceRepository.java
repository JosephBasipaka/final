package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Service;

public interface ServiceRepository extends JpaRepository<Service, Long> {
        Service findByCustomer(Customer customer);

        Service findByServiceName(String serviceNameString);

        Service findByCustomerIdAndServiceName(Long customerId, String serviceName);

        @Query("SELECT s FROM Service s WHERE s.customer.name = :customerName AND s.serviceName = :serviceName")
        Service findByCustomerNameAndServiceName(
                        @Param("customerName") String customerName,
                        @Param("serviceName") String serviceName);
}
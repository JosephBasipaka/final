package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Dunning;
import com.example.demo.entity.Service;


public interface DunningRepository extends JpaRepository<Dunning, Long> {
    @Query("SELECT d FROM Dunning d WHERE d.customer = :customer")
    List<Dunning> findDunningStepsForCustomer(@Param("customer") Customer customer);


    Dunning findByCustomerAndStepNameAndStatusAndService(
        Customer customer,
        String stepName,
        String status,
        Service service
    );
}
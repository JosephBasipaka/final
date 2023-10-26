package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Customer;
import com.example.demo.entity.PaymentPlan;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.service.CuringService;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/curing")
@CrossOrigin(origins = "*")
public class CuringController {

    @Autowired
    private CuringService curingService;

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/createPaymentPlan")
    public ResponseEntity<PaymentPlan> createPaymentPlan(
            @RequestParam Long customerId,
            @RequestParam double totalAmount,
            @RequestParam LocalDate dueDate,
            @RequestParam int numberOfInstallments) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        PaymentPlan paymentPlan = curingService.createPaymentPlan(customer, totalAmount, dueDate, numberOfInstallments);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentPlan);
    }

    @PostMapping("/trackPayment")
    public ResponseEntity<PaymentPlan> trackPayment(
            @RequestParam Long customerId,
            @RequestParam Long paymentPlanId,
            @RequestParam double paymentAmount,
            @RequestParam String servicePlan) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        PaymentPlan paymentPlan = curingService.getPaymentPlansByCustomer(customer);
        if (customer == null || paymentPlan == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        curingService.trackPayment(customer, paymentPlan, servicePlan, paymentAmount);
        return ResponseEntity.status(HttpStatus.OK).body(paymentPlan);
    }

    @GetMapping("/paymentPlansByCustomer/{id}")
    public ResponseEntity<PaymentPlan> getPaymentPlansByCustomer(@PathVariable Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        PaymentPlan paymentPlan = curingService.getPaymentPlansByCustomer(customer);
        return ResponseEntity.status(HttpStatus.OK).body(paymentPlan);
    }
}

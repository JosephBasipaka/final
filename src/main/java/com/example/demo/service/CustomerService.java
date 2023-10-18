package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Invoice;
import com.example.demo.repositories.CustomerRepository;

@Service
public class CustomerService {
    

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public List<Customer> findOverdueCustomers() {
        return customerRepository.findCustomersWithOverdueInvoices();
    }
    
    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
    public Customer findId(String name, String email){
       return customerRepository.findByNameAndEmail(name, email);
    }
    
    public Customer updateCustomer(Long customerId, Customer customer) {
        if (customerRepository.existsById(customerId)) {
            customer.setId(customerId);
            return customerRepository.save(customer);
        } else {
            return null; 
        }
    }

    public boolean deleteCustomer(Long customerId) {
        if (customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);
            return true;
        } else {
            return false; 
        }
    }

    public boolean isOverdue(Customer customer) {
        List<Invoice> invoices = customer.getInvoices();
        if (invoices == null || invoices.isEmpty()) {
            return false;
        }
        LocalDate today = LocalDate.now();
        System.out.println(today);
        for (Invoice invoice : invoices) {
            // && !invoice.getPaid()
            if (invoice.getDueDate() != null  && invoice.getDueDate().isBefore(today)) {
                return true;
            }
        }
        return false;
    }
}

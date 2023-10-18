package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Invoice;
import com.example.demo.entity.Service;
import com.example.demo.service.CustomerService;
import com.example.demo.service.InvoiceService;
import com.example.demo.service.ServiceService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class finalRestController {

    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private ServiceService serviceService;

    @Autowired
    private InvoiceService invoiceService;
    
    @GetMapping("/start")
    public String start(){
        return "Starting Capstone Project";
    }

    @GetMapping("/customers")
    public List<Customer> customerList(){
        return customerService.getAllCustomers();
    }

    @GetMapping("/invoice")
    public List<Invoice> InvoiceList(){
        return invoiceService.InvoiceList();
    }

    @GetMapping("/services")
    public List<Service> ServiceList(){
        return serviceService.serviceList();
    }

    @GetMapping("/works")
    public String starting(){
        return "heyy I am working";
    }

    @PostMapping("/addCustomer")
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.createCustomer(customer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }
    @PostMapping("/addService")
    public ResponseEntity<Service> addService(@RequestBody Service service) {
        Service newService = serviceService.createService(service);
        // System.out.println("service" + newService + "service customer" + newService.getCustomer());
        return new ResponseEntity<>(newService, HttpStatus.CREATED);
    }
    @PostMapping("/addInvoice")
    public ResponseEntity<Invoice> addInvoice(@RequestBody Invoice invoice) {
        Invoice newInvoice = invoiceService.addInvoice(invoice);
        return new ResponseEntity<>(newInvoice , HttpStatus.CREATED);
    }

    @GetMapping("/customer/id")
    public ResponseEntity<Long> getCustomerId(@RequestParam String name, @RequestParam String email) {
        // Query the customer by name and email
        Customer customer = customerService.findId(name,email);
        if (customer != null) {
            return new ResponseEntity<>(customer.getId(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

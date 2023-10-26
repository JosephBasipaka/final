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
public class FinalRestController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> customerList() {
        return ResponseEntity.ok().body(customerService.getAllCustomers());
    }

    @GetMapping("/find")
    public ResponseEntity<Customer> getCustomer(@RequestParam String customerName, @RequestParam String serviceName) {
        Customer customer = customerService.getCustomerAndService(customerName, serviceName);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else
            return ResponseEntity.notFound().build();
    }

    @PostMapping("/addCustomer")
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.createCustomer(customer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    @PostMapping("/addService")
    public ResponseEntity<Service> addService(@RequestBody Service service) {
        Service newService = serviceService.createService(service);
        return new ResponseEntity<>(newService, HttpStatus.CREATED);
    }

    @PostMapping("/addInvoice")
    public ResponseEntity<Invoice> addInvoice(@RequestBody Invoice invoice) {
        Invoice newInvoice = invoiceService.addInvoice(invoice);
        return new ResponseEntity<>(newInvoice, HttpStatus.CREATED);
    }

    @GetMapping("/customer/id")
    public ResponseEntity<Customer> getCustomerId(@RequestParam String name, @RequestParam String email) {
        Customer customer = customerService.findId(name, email);
        if (customer != null) {
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/service/id")
    public ResponseEntity<Service> getServiceId(@RequestParam Long customerId, @RequestParam String serviceName) {
        Service service = serviceService.findId(customerId, serviceName);
        if (service != null) {
            return new ResponseEntity<>(service, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

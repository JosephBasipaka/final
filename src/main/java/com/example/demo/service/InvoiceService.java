package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// import com.example.demo.entity.Customer;
import com.example.demo.entity.Invoice;
// import com.example.demo.repositories.CustomerRepository;
import com.example.demo.repositories.InvoiceRepository;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;
    // @Autowired
    // private CustomerRepository customerRepository;

    public List<Invoice> InvoiceList(){
        return invoiceRepository.findAll();
    }    

    public Invoice addInvoice(Invoice invoice){
        return invoiceRepository.save(invoice);
    }
}

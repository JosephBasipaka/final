package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.demo.entity.Invoice;
import com.example.demo.repositories.InvoiceRepository;
import com.example.demo.service.InvoiceService;

@SpringBootTest
 class InvoiceServiceTest {
    
    @Autowired
    private InvoiceService invoiceService;

    @MockBean
    private InvoiceRepository invoiceRepository;

    

    @Test
     void testInvoiceList_Success() {
        List<Invoice> invoices = new ArrayList<>();
        invoices.add(new Invoice(1L,5000.0,LocalDate.now(), Boolean.TRUE,null,null));
        invoices.add(new Invoice(2L,3000.0,LocalDate.now(), Boolean.TRUE,null,null));
        Mockito.when(invoiceRepository.findAll()).thenReturn(invoices);

        List<Invoice> result = invoiceService.InvoiceList();
        assertEquals(2, result.size());
    }
    @Test
     void testInvoiceList_Fail() {
        List<Invoice> invoices = new ArrayList<>();
        invoices.add(new Invoice(1L,5000.0,LocalDate.now(), Boolean.TRUE,null,null));
        Mockito.when(invoiceRepository.findAll()).thenReturn(invoices);

        List<Invoice> result = invoiceService.InvoiceList();
        assertNotEquals(2, result.size());
    }

    @Test
     void testAddInvoice_Success() {
        Invoice invoice = new Invoice(1L,5000.0,LocalDate.now(), Boolean.TRUE,null,null);
        Mockito.when(invoiceRepository.save(invoice)).thenReturn(invoice);

        Invoice createdInvoice = invoiceService.addInvoice(invoice);
        assertEquals(invoice, createdInvoice);
    }
    @Test
     void testAddInvoice_Fail() {
        Invoice invoice = new Invoice(1L,5000.0,LocalDate.now(), Boolean.TRUE,null,null);
        Mockito.when(invoiceRepository.save(invoice)).thenReturn(invoice);

        Invoice test = new Invoice();
        Invoice createdInvoice = invoiceService.addInvoice(test);
        assertNotEquals(invoice, createdInvoice);
    }

}

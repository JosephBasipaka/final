package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Invoice;
import com.example.demo.entity.Service;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.service.CustomerService;

@SpringBootTest
 class CustomerServiceTest {
    
    @Autowired
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepository;

    @BeforeEach
    void setup(){
        List<Service> ser = new ArrayList<>();
        List<Invoice> invoice = new ArrayList<>();
        Optional<Customer> customer = Optional.of(new Customer((long) 1,"joseph","j@gmail.com",invoice, ser));
        Mockito.when(customerRepository.findById(1L)).thenReturn(customer);
    }

    @Test
    void testGetCustomerById_Success(){
        String name = "joseph";
        Customer customerById = customerService.getCustomerById((long)1);
        assertEquals(name , customerById.getName());
        assertNotEquals("jo" , customerById.getName());
    }

    @Test
    void testGetAllCustomers_Success() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer(1L, "John", "john@gmail.com", null, null));
        customers.add(new Customer(2L, "Joseph", "joseph@gmail.com", null, null));
        Mockito.when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> result = customerService.getAllCustomers();
        assertEquals(2, result.size());
    }

    @Test
     void testCreateCustomer_Success() {
        Customer newCustomer = new Customer(3L, "jo", "jo@gmail.com", null, null);
        Mockito.when(customerRepository.save(newCustomer)).thenReturn(newCustomer);

        Customer createdCustomer = customerService.createCustomer(newCustomer);
        assertEquals(newCustomer, createdCustomer);
    }

    @Test
     void testFindId_Success() {
        String name = "j";
        String email = "j@gmail.com";
        Customer expectedCustomer = new Customer(1L, name, email, null, null);
        Mockito.when(customerRepository.findByNameAndEmail(name, email)).thenReturn(expectedCustomer);

        Customer foundCustomer = customerService.findId(name, email);

        assertEquals(expectedCustomer, foundCustomer);
    }

     @Test
     void testUpdateCustomer_Success() {
        Long customerId = 1L;
        Customer updatedCustomer = new Customer(customerId, "jo", "j@gmail.com", null, null);
        Mockito.when(customerRepository.existsById(customerId)).thenReturn(true);
        Mockito.when(customerRepository.save(updatedCustomer)).thenReturn(updatedCustomer);

        Customer updated = customerService.updateCustomer(customerId, updatedCustomer);
        assertEquals(updatedCustomer, updated);
    }

    @Test
     void testUpdateCustomer_NotFound() {
        Long customerId = 1L;
        Customer updatedCustomer = new Customer(customerId, "jo", "j@gmail.com", null, null);
        Mockito.when(customerRepository.existsById(customerId)).thenReturn(false);

        Customer updated = customerService.updateCustomer(customerId, updatedCustomer);
        assertNull(updated);
    }

    @Test
     void testDeleteCustomer_Success() {
        Long customerId = 1L;
        Mockito.when(customerRepository.existsById(customerId)).thenReturn(true);

        boolean deleted = customerService.deleteCustomer(customerId);
        assertTrue(deleted);
    }

    @Test
     void testDeleteCustomer_NotFound() {
        Long customerId = 1L;
        Mockito.when(customerRepository.existsById(customerId)).thenReturn(false);

        boolean deleted = customerService.deleteCustomer(customerId);
        assertFalse(deleted);
    }

    @Test
     void testIsOverdue_NoInvoices() {
        Customer customer = new Customer(1L , "jo", "j@gmail.com", null, null);
        customer.setInvoices(new ArrayList<>());

        boolean result = customerService.isOverdue(customer);
        assertFalse(result);
    }

    @Test
     void testIsOverdue_AllInvoicesPaid() {
        Customer customer = new Customer(1L , "jo", "j@gmail.com", null, null);
        List<Invoice> invoices = new ArrayList<>();
        invoices.add(new Invoice(1L,5000.0,LocalDate.now(), Boolean.TRUE,null,null)); 
        customer.setInvoices(invoices);

        boolean result = customerService.isOverdue(customer);
        assertFalse(result);
    }

    @Test
     void testIsOverdue_SomeInvoicesOverdue() {
        Customer customer = new Customer(1L , "jo", "j@gmail.com", null, null);
        List<Invoice> invoices = new ArrayList<>();
        invoices.add(new Invoice(1L,5000.0,LocalDate.now().minusDays(1), Boolean.FALSE,null,null));  // Overdue, not paid
        invoices.add(new Invoice(2L,3000.0,LocalDate.now().plusDays(1), Boolean.FALSE,null,null));  // Future due date, not paid
        customer.setInvoices(invoices);

        boolean result = customerService.isOverdue(customer);

        assertTrue(result);
    }

    @Test
     void testIsOverdue_AllInvoicesOverdue() {
        Customer customer = new Customer(1L , "jo", "j@gmail.com", null, null);
        List<Invoice> invoices = new ArrayList<>();
        invoices.add(new Invoice(1L,5000.0,LocalDate.now().minusDays(1), Boolean.FALSE,customer,null));  // Overdue, not paid
        invoices.add(new Invoice(2L,3000.0,LocalDate.now().minusDays(2), Boolean.FALSE,null,null)); // Overdue, not paid
        customer.setInvoices(invoices);

        boolean result = customerService.isOverdue(customer);
        assertTrue(result);
    }

    @Test
     void testFindOverdueCustomers_Success() {
        List<Invoice> invoices = new ArrayList<>();
        invoices.add(new Invoice(1L,5000.0,LocalDate.now().minusDays(5), Boolean.TRUE,null,null));
        invoices.add(new Invoice(2L,3000.0,LocalDate.now().plusDays(2), Boolean.TRUE,null,null));
        Customer customer1 = new Customer(1L, "Joseph", "joseph@gmail.com", invoices,null);
        Customer customer2 = new Customer(2L, "John", "john@gmail.com", invoices, null);
        List<Customer> overdueCustomers = Arrays.asList(customer1, customer2);

        Mockito.when(customerRepository.findCustomersWithOverdueInvoices()).thenReturn(overdueCustomers);

        List<Customer> result = customerService.findOverdueCustomers();
        assertEquals(overdueCustomers, result);
    }
}

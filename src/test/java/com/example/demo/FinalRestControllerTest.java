package com.example.demo;

import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import com.example.demo.controller.DunningScheduler;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Invoice;
import com.example.demo.entity.Service;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.repositories.InvoiceRepository;
import com.example.demo.repositories.ServiceRepository;
import com.example.demo.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.scheduled.enabled=false" 
})
class FinalRestControllerTest {


    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private DunningScheduler dunningScheduler;
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private static HttpHeaders headers;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @Sql(statements = "INSERT INTO customer(id, name, email) VALUES (22,'john', 'jo@gmial.com')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM customer WHERE id='22'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testCustomer() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<List<Customer>> response = restTemplate.exchange(
                createURLWithPort("/customers"), HttpMethod.GET, entity, new ParameterizedTypeReference<List<Customer>>(){});
        List<Customer> customers = response.getBody();
        assert customers != null;
        assertEquals(200, response.getStatusCode().value());
        assertEquals(customers.size(), customerService.getAllCustomers().size());
        assertEquals(customers.size(), customerRepository.findAll().size());
        System.out.println(customers);
    }

     @Test
    @Sql(statements = "INSERT INTO customer(id, name, email) VALUES (22,'john', 'jo@gmail.com')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM customer WHERE id='22'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testOrderById() throws JsonProcessingException {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<Long> response = restTemplate.exchange(
                (createURLWithPort("/customer/id?name=john&email=jo@gmail.com")), HttpMethod.GET, entity, new ParameterizedTypeReference<Long>(){});
        Long customer = response.getBody();
        String outcome = "{\"id\":22,\"name\":\"john\",\"email\":\"jo@gmail.com\",\"invoices\":[],\"services\":[]}";
        assert customer != null;
        assertEquals(outcome, objectMapper.writeValueAsString(customerService.findId("john", "jo@gmail.com")));
        assertEquals(outcome, objectMapper.writeValueAsString(customerRepository.findById(22L).orElse(null)));
    }
     @Test
    @Sql(statements = "INSERT INTO customer(id, name, email) VALUES (22,'john', 'jo@gmail.com')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM customer WHERE id='22'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testOrderByIdFail() throws JsonProcessingException {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<Long> response = restTemplate.exchange(
                (createURLWithPort("/customer/id?name=jo&email=jo@gmail.com")), HttpMethod.GET, entity, new ParameterizedTypeReference<Long>(){});
        Long customer = response.getBody();
        assert customer == null;
        assertEquals(404, response.getStatusCode().value());
        
    }
    @Test
    @Sql(statements = "DELETE FROM customer WHERE id='40'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testCreateCustomer() throws JsonProcessingException {
        Customer customer = new Customer(40L, "j", "j@gmail.com", null, null);
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(customer), headers);
        ResponseEntity<Customer> response = restTemplate.exchange(
                createURLWithPort("/addCustomer"), HttpMethod.POST, entity, Customer.class);
        assertEquals(201,response.getStatusCode().value());
        Customer resService = Objects.requireNonNull(response.getBody());
        assertEquals("j", resService.getName());
        assertEquals(resService.getEmail(), customerRepository.save(customer).getEmail());
    }
    @Test
    @Sql(statements = "DELETE FROM invoice WHERE id='40'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testCreateInvoice() throws JsonProcessingException {
        Invoice invoice = new Invoice(40L,5000.0,null, Boolean.TRUE,null,null);
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(invoice), headers);
        ResponseEntity<Invoice> response = restTemplate.exchange(
                createURLWithPort("/addInvoice"), HttpMethod.POST, entity, Invoice.class);
        assertEquals(201,response.getStatusCode().value());
        Invoice resService = Objects.requireNonNull(response.getBody());
        assertEquals(resService.getAmount(), invoiceRepository.save(invoice).getAmount());
    }
    @Test
    @Sql(statements = "DELETE FROM service WHERE id='40'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testCreateService() throws JsonProcessingException {
        Service service = new Service(40L, "basic" , 1000, "Active", null, null);
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(service), headers);
        ResponseEntity<Service> response = restTemplate.exchange(
                createURLWithPort("/addService"), HttpMethod.POST, entity, Service.class);
        assertEquals(201,response.getStatusCode().value());
        Service resService = Objects.requireNonNull(response.getBody());
        assertEquals("basic", resService.getServiceName());
        assertEquals(resService.getServiceCost(), serviceRepository.save(service).getServiceCost());
    }

     @Test
    void testDunningIntitate() throws JsonProcessingException {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/dunning/intReminder"), HttpMethod.POST, entity,new ParameterizedTypeReference<String>(){});
        dunningScheduler.scheduleFollowUpReminder();
        assertEquals(200,response.getStatusCode().value());
        assertEquals("Scheduled Initial Reminder executed immediately.", response.getBody());
    }
    private String createURLWithPort(String path) {
        return "http://localhost:" + port + "/api" + path;
    }


}

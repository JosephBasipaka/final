package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.controller.finalRestController;
import com.example.demo.entity.Customer;
import com.example.demo.service.CustomerService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.when;

@WebMvcTest(finalRestController.class)
class finalRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    void testCustomerList() throws Exception {
        Customer customer1 = new Customer(1L, "John", "john@example.com", new ArrayList<>(), new ArrayList<>());
        Customer customer2 = new Customer(2L, "Alice", "alice@example.com", new ArrayList<>(), new ArrayList<>());

        when(customerService.getAllCustomers()).thenReturn(Arrays.asList(customer1, customer2));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk()) 
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // .andExpect(jsonPath("$", hasSize(2)))  // Expect an array with 2 elements
                .andExpect(jsonPath("$[0].id", is(1L)))  // Verify the first customer's ID
                .andExpect(jsonPath("$[0].name", is("John"))) // Verify the first customer's name
                .andExpect(jsonPath("$[1].id", is(2)))  // Verify the second customer's ID
                .andExpect(jsonPath("$[1].name", is("Alice"))); // Verify the second customer's name
    }

    private Object is(long l) {
        return null;
    }
}

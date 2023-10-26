package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.entity.PaymentPlan;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.scheduled.enabled=false"
})
public class CuringControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static HttpHeaders headers;

    @BeforeAll
    public static void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    private String createURLWithPort(String path) {
        return "http://localhost:" + port + "/api" + path;
    }

    @Test
    @Sql(statements = { "INSERT INTO customer(id, name, email) VALUES (40, 'kim', 'kim@example.com')",
            "INSERT INTO payment_plan(id, due_date, installment_amount,number_of_installments, start_date, status, total_amount, customer_id) VALUES(50, '2023-10-25', 2500.0, 2, '2023-10-25', 'ACTIVE', 5000.0, 40)" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = { "DELETE FROM payment_plan WHERE id='50'",
            "DELETE FROM customer WHERE id='40'" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testCustomer() {
        try {
            HttpEntity<String> entity = new HttpEntity<>(null, headers);
            ResponseEntity<PaymentPlan> response = restTemplate.exchange(
                    createURLWithPort("/curing/paymentPlansByCustomer/40"), HttpMethod.GET, entity,
                    new ParameterizedTypeReference<PaymentPlan>() {
                    });
            PaymentPlan paymentPlan = response.getBody();
            assert paymentPlan != null;
            assertEquals(200, response.getStatusCode().value());
            assertEquals(paymentPlan, response.getBody());
        } catch (RestClientException e) {
            System.err.println("Error: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @Sql(statements = {
            "INSERT INTO customer(id, name, email) VALUES (200, 'kim', 'kim@example.com')" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = { "DELETE FROM payment_plan WHERE customer_id=200",
            "DELETE FROM customer WHERE id=200" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testCreatePaymentPlan() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/curing/createPaymentPlan")
                .queryParam("customerId", "200")
                .queryParam("totalAmount", "5000.0")
                .queryParam("dueDate", "2023-11-15")
                .queryParam("numberOfInstallments", "2");
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<PaymentPlan> response = restTemplate.exchange(
                createURLWithPort(builder.toUriString()), HttpMethod.POST, entity,
                new ParameterizedTypeReference<PaymentPlan>() {
                });

        PaymentPlan paymentPlan = response.getBody();
        assert paymentPlan != null;
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @Sql(statements = {
            "INSERT INTO customer(id, name, email) VALUES (800, 'kim', 'kim@example.com')",
            "INSERT INTO payment_plan(id, due_date, installment_amount, number_of_installments, start_date, status, total_amount, customer_id) VALUES(500, '2023-10-25', 2500.0, 2, '2023-10-25', 'ACTIVE', 5000.0, 800)"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM payment WHERE customer_id=800",
            "DELETE FROM payment_plan WHERE customer_id=800",
            "DELETE FROM invoice WHERE customer_id=800",
            "DELETE FROM service WHERE customer_id=800",
            "DELETE FROM customer WHERE id=800"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testTrackPayment() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/curing/trackPayment")
                .queryParam("customerId", "800")
                .queryParam("paymentPlanId", "500")
                .queryParam("paymentAmount", "2500.0")
                .queryParam("servicePlan", "premium");
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<PaymentPlan> response = restTemplate.exchange(
                createURLWithPort(builder.toUriString()), HttpMethod.POST, entity,
                new ParameterizedTypeReference<PaymentPlan>() {
                });

        PaymentPlan paymentPlan = response.getBody();
        assert paymentPlan != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}

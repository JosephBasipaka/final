package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Invoice;
import com.example.demo.entity.Payment;
import com.example.demo.entity.PaymentPlan;
import com.example.demo.entity.PaymentStatus;
import com.example.demo.entity.Reminder;
import com.example.demo.entity.Service;
import com.example.demo.repositories.InvoiceRepository;
import com.example.demo.repositories.PaymentPlanRepository;
import com.example.demo.repositories.PaymentRepository;
import com.example.demo.repositories.ServiceRepository;
import com.example.demo.service.CuringService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class CuringServiceTest {

    @Autowired
    private CuringService curingService;

    @MockBean
    private PaymentPlanRepository paymentPlanRepository;
    @MockBean
    private ServiceRepository serviceRepository;
    @MockBean
    private InvoiceRepository invoiceRepository;
    @MockBean
    private PaymentRepository paymentRepository;

    @Test
    void testGetPaymentPlansByCustomer() {
        Customer customer = new Customer(2L, "Joseph", "joseph@gmail.com", null, null, null, null);

        List<Payment> payments = new ArrayList<>();
        PaymentPlan expectedPaymentPlan = new PaymentPlan(1L, customer, 5000, LocalDate.now().plusDays(5), 2, 2500,
                LocalDate.now(),
                PaymentStatus.ACTIVE, payments);

        when(paymentPlanRepository.findByCustomer(customer)).thenReturn(expectedPaymentPlan);

        PaymentPlan actualPaymentPlan = curingService.getPaymentPlansByCustomer(customer);

        verify(paymentPlanRepository).findByCustomer(customer);
        assertEquals(expectedPaymentPlan, actualPaymentPlan);
    }

    @Test
    void testCreatePaymentPlan() {
        Customer customer = new Customer(2L, "Joseph", "joseph@gmail.com", null, null, null, null);

        List<Payment> payments = new ArrayList<>();
        PaymentPlan expectedPaymentPlan = new PaymentPlan(1L, customer, 5000, LocalDate.now().plusDays(5), 2, 2500,
                LocalDate.now(),
                PaymentStatus.ACTIVE, payments);

        when(paymentPlanRepository.save(any(PaymentPlan.class))).thenReturn(expectedPaymentPlan);
        PaymentPlan createdPaymentPlan = curingService.createPaymentPlan(customer, 5000, LocalDate.now().plusDays(5),
                2);
        assertEquals(expectedPaymentPlan, createdPaymentPlan);
    }

    @Test
    void testTrackPayment() {
        List<Service> services = new ArrayList<>();
        services.add(new Service(1L, "basic", 1000, "Active", null, null));
        Customer customer = new Customer(2L, "Joseph", "joseph@gmail.com", null, services, null, null);
        List<Payment> payments = new ArrayList<>();
        PaymentPlan expectedPaymentPlan = new PaymentPlan(1L, customer, 1000, LocalDate.now().plusDays(5), 2, 500,
                LocalDate.now(),
                PaymentStatus.ACTIVE, payments);
        String servicePlan = "basic";
        double paymentAmount = 1000.00;
        Invoice invoice = new Invoice();
        for (Service service : services) {
            when(serviceRepository.findByCustomerIdAndServiceName(customer.getId(), servicePlan)).thenReturn(service);
            invoice = new Invoice(4L, paymentAmount, LocalDate.now(), Boolean.FALSE, customer, service);
        }
        when(invoiceRepository.findByCustomerIdAndPaid(customer.getId(), false)).thenReturn(invoice);
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);
        when(paymentRepository.save(any(Payment.class))).thenReturn(null);

        curingService.trackPayment(customer, expectedPaymentPlan, servicePlan, paymentAmount);

        verify(serviceRepository).findByCustomerIdAndServiceName(customer.getId(), servicePlan);
        verify(invoiceRepository).findByCustomerIdAndPaid(customer.getId(), false);

        verify(serviceRepository).save(any(Service.class));
        verify(invoiceRepository).save(any(Invoice.class));
        assertEquals(0, invoice.getAmount());
    }

    @Test
    void testTrackPayment_NotNullServicesAndInvoices() {
        Customer customer = new Customer(2L, "Joseph", "joseph@gmail.com", null, null, null, null);
        List<Payment> payments = new ArrayList<>();
        PaymentPlan expectedPaymentPlan = new PaymentPlan(1L, customer, 1000, LocalDate.now().plusDays(5), 2, 500,
                LocalDate.now(),
                PaymentStatus.ACTIVE, payments);
        String servicePlan = "basic";
        double paymentAmount = 1000.00;
        when(serviceRepository.findByCustomerIdAndServiceName(customer.getId(), servicePlan)).thenReturn(null);
        when(invoiceRepository.findByCustomerIdAndPaid(customer.getId(), false)).thenReturn(null);
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(null);
        when(paymentRepository.save(any(Payment.class))).thenReturn(null);

        curingService.trackPayment(customer, expectedPaymentPlan, servicePlan, paymentAmount);

        verify(serviceRepository).findByCustomerIdAndServiceName(customer.getId(), servicePlan);
        verify(invoiceRepository).findByCustomerIdAndPaid(customer.getId(), false);

        verify(serviceRepository).save(any(Service.class));
    }

    @Test
    void ReminderTest() {
        Customer customer = new Customer(40L, "trump", "trump@gmail.com", null, null, null, null);
        Reminder reminder = new Reminder();
        reminder.setCustomer(customer);
        reminder.setContent("Reminder Sent");
        reminder.setTimestamp(new Date());
        reminder.setId(20L);
        Reminder reminder2 = new Reminder(21L, "Reminder not sent", new Date(), customer);
        assertEquals(customer, reminder.getCustomer());
        assertEquals("Reminder Sent", reminder.getContent());
        assertEquals(21L, reminder2.getId());
        assertEquals(new Date(), reminder2.getTimestamp());

    }

    @Test
    void PaymentTest() {
        Customer customer = new Customer(40L, "trump", "trump@gmail.com", null, null, null, null);
        List<Payment> payments = null;
        PaymentPlan paymentPlan = new PaymentPlan(50L, customer, 5000.0, LocalDate.now(), 2, 2500.0, LocalDate.now(),
                PaymentStatus.ACTIVE, payments);
        Payment payment = new Payment(20L, customer, paymentPlan, 5000.0, LocalDate.now(), PaymentStatus.RECEIVED);

        assertEquals(paymentPlan, payment.getPaymentPlan());
        assertEquals(customer, payment.getCustomer());
    }

}

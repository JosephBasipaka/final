package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Invoice;
import com.example.demo.entity.Payment;
import com.example.demo.entity.PaymentPlan;
import com.example.demo.entity.PaymentStatus;
import com.example.demo.entity.Service;
import com.example.demo.repositories.InvoiceRepository;
import com.example.demo.repositories.PaymentPlanRepository;
import com.example.demo.repositories.PaymentRepository;
import com.example.demo.repositories.ServiceRepository;

import java.time.LocalDate;

@org.springframework.stereotype.Service
public class CuringService {

    @Autowired
    private PaymentPlanRepository paymentPlanRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Transactional
    public PaymentPlan createPaymentPlan(Customer customer, double totalAmount, LocalDate dueDate,
            int numberOfInstallments) {
        PaymentPlan paymentPlan = new PaymentPlan();
        paymentPlan.setCustomer(customer);
        paymentPlan.setTotalAmount(totalAmount);
        paymentPlan.setDueDate(dueDate);
        paymentPlan.setNumberOfInstallments(numberOfInstallments);
        paymentPlan.setInstallmentAmount(totalAmount / numberOfInstallments);
        paymentPlan.setStartDate(LocalDate.now());
        paymentPlan.setStatus(PaymentStatus.ACTIVE);
        paymentPlan = paymentPlanRepository.save(paymentPlan);

        return paymentPlan;
    }

    @Transactional
    public void trackPayment(Customer customer, PaymentPlan paymentPlan, String servicePlan, double paymentAmount) {
        Payment payment = new Payment();
        payment.setCustomer(customer);
        payment.setPaymentPlan(paymentPlan);
        payment.setAmount(paymentAmount);
        payment.setPaymentDate(LocalDate.now());
        payment.setStatus(PaymentStatus.RECEIVED);

        Service existingService = serviceRepository.findByCustomerIdAndServiceName(customer.getId(), servicePlan);
        if (existingService == null) {
            Service newService = new Service();
            newService.setCustomer(customer);
            newService.setServiceName(servicePlan);
            newService.setServiceCost(paymentAmount);
            newService.setStatus("Active");
            serviceRepository.save(newService);
        } else {
            existingService.setServiceCost(existingService.getServiceCost() - paymentAmount);
            serviceRepository.save(existingService);
        }

        paymentRepository.save(payment);
        LocalDate dueDate = LocalDate.now().plusDays(30);
        Invoice invoice = invoiceRepository.findByCustomerIdAndPaid(customer.getId(), false);
        if (invoice == null) {
            Invoice newInvoice = new Invoice();
            newInvoice.setCustomer(customer);
            newInvoice.setAmount(paymentAmount);
            newInvoice.setDueDate(dueDate);
            newInvoice.setPaid(true);
            invoiceRepository.save(newInvoice);
        } else {
            invoice.setAmount(invoice.getAmount() - paymentAmount);
            invoice.setDueDate(dueDate);
            invoiceRepository.save(invoice);
        }

    }

    public PaymentPlan getPaymentPlansByCustomer(Customer customer) {
        return paymentPlanRepository.findByCustomer(customer);
    }
}

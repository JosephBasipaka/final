package com.example.demo.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PaymentPlan implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference(value = "paymentPlan-customer")
    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;
    private double totalAmount;
    private LocalDate dueDate;
    private int numberOfInstallments;
    private double installmentAmount;
    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @JsonManagedReference(value = "payment-paymentPlan")
    @OneToMany(mappedBy = "paymentPlan", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Payment> payments;

}

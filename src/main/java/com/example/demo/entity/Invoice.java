package com.example.demo.entity;

import java.time.LocalDate;
// import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    private LocalDate dueDate;
    private Boolean paid;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    private Service service;

    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + id +
            ", amount=" + amount +
            ", dueDate=" + dueDate +
            ", paid=" + paid +
            '}';
    }

    public Invoice(Long id, Double amount, LocalDate dueDate, Boolean paid, Customer customer,Service service){
        this.id = id;
        this.amount = amount;
        this.dueDate = dueDate;
        this.customer = customer;
        this.service = service;
    }

    public Invoice() {
    }
}


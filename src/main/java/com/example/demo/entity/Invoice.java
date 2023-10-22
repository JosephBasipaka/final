package com.example.demo.entity;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    private LocalDate dueDate;
    private Boolean paid;

    @JsonBackReference(value="invoice-customer")
    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;

    @JsonBackReference(value="invoice-service")
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

}


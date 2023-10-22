package com.example.demo.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class PaymentPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Customer customer;
    private double totalAmount;
    private LocalDate dueDate;
    private int numberOfInstallments;
    private double installmentAmount;
    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @OneToMany(mappedBy = "paymentPlan", cascade = CascadeType.ALL)
    private List<Payment> payments;

    @Override
    public String toString() {
        return "PaymentPlan{" +
            "id=" + id +
            ", date='" + dueDate + '\'' +
            ", amount='" + totalAmount + '\'' +
            // Include other relevant fields, but avoid circular references
            '}';
    }
}

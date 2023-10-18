package com.example.demo.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Service{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String serviceName;
    private double serviceCost;
    private String status;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @JsonManagedReference
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Invoice> invoices;

    @Override
    public String toString() {
        return "Service{" +
            "id=" + id +
            ", serviceName=" + serviceName +
            ", serViceCost=" + serviceCost +
            ", status=" + status +
            '}';
    }

    public Service(Long id, String serviceName, double serviceCost, String status, Customer customer, List<Invoice> invoices){
        this.id = id;
        this.serviceName = serviceName;
        this.serviceCost = serviceCost;
        this.status = status;
        this.customer = customer;
        this.invoices = invoices;
    }
    public Service(){

    }
}

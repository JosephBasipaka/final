package com.example.demo.entity;
public enum PaymentStatus {
    ACTIVE,
    PENDING,    // Payment has been initiated but not yet received.
    RECEIVED,   // Payment has been received and processed successfully.
    LATE,       // Payment is late, past its due date.
    FAILED,     // Payment failed or encountered an issue during processing.
    REFUNDED,   // Payment was refunded to the customer.
    SETTLED     // Payment has been settled or cleared.
    // You can add more status values as needed.
}

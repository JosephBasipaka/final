package com.example.demo.entity;

public enum PaymentStatus {

    ACTIVE,
    PENDING, // Payment has been initiated but not yet received.
    RECEIVED, // Payment has been received and processed successfully.
}

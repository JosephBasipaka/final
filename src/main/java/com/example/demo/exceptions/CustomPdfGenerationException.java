package com.example.demo.exceptions;

public class CustomPdfGenerationException extends Exception {
    public CustomPdfGenerationException(String message) {
        super(message);
    }

    public CustomPdfGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}

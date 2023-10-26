package com.example.demo.exceptions;

public class TemporaryDirectoryCreationException extends RuntimeException {
    public TemporaryDirectoryCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}

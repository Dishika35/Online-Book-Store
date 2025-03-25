package com.example.bookstore.exception;

// Custom exception for handling resource not found errors
public class ResourceNotFoundException extends RuntimeException {
    
    // Constructor that takes an error message and passes it to the superclass
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

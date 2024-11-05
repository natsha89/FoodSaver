package com.natasha.foodsaver.exception;

public class EmailNotVerifiedException extends RuntimeException {
    // Constructor that accepts a message
    public EmailNotVerifiedException(String message) {
        super(message); // Call the superclass constructor with the message
    }

    // Constructor that accepts both a message and a cause
    public EmailNotVerifiedException(String message, Throwable cause) {
        super(message, cause); // Call the superclass constructor with the message and cause
    }
}

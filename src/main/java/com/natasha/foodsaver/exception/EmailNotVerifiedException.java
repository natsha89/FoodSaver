package com.natasha.foodsaver.exception;

// Exception class that represents the case when a user's email is not verified
public class EmailNotVerifiedException extends RuntimeException {

    // Constructor that accepts a custom error message
    public EmailNotVerifiedException(String message) {
        super(message); // Call the superclass (RuntimeException) constructor with the provided message
    }
}

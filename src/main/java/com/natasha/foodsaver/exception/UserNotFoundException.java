package com.natasha.foodsaver.exception;

// Custom exception class to handle cases where a user is not found
public class UserNotFoundException extends RuntimeException {

    // Constructor that accepts a custom message as a parameter
    public UserNotFoundException(String message) {
        super(message); // Call the superclass constructor (RuntimeException) with the provided message
    }
}

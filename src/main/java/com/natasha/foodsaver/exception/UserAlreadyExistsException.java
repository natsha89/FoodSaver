package com.natasha.foodsaver.exception;

// Custom exception class to handle cases where a user already exists
public class UserAlreadyExistsException extends RuntimeException {

    // Constructor that accepts a custom message as a parameter
    public UserAlreadyExistsException(String message) {
        super(message); // Call the superclass constructor (RuntimeException) with the message
    }
}

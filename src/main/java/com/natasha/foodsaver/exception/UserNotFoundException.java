package com.natasha.foodsaver.exception;

/*
 * MIT License
 * Copyright (c) [2024] [Natasha Shahran]
 *
 * Permission is granted under the MIT License to use, modify, and distribute
 * this software, provided credit is given to the original creator ([Natasha Shahran]).
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 */

// Custom exception class to handle cases where a user is not found
public class UserNotFoundException extends RuntimeException {

    // Constructor that accepts a custom message as a parameter
    public UserNotFoundException(String message) {
        super(message); // Call the superclass constructor (RuntimeException) with the provided message
    }
}

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

// Exception class that represents the case when a user's email is not verified
public class EmailNotVerifiedException extends RuntimeException {

    // Constructor that accepts a custom error message
    public EmailNotVerifiedException(String message) {
        super(message); // Call the superclass (RuntimeException) constructor with the provided message
    }
}

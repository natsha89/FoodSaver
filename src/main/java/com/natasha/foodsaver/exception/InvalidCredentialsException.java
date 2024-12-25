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

// En exception som kastas när användarens inloggningsuppgifter är ogiltiga
public class InvalidCredentialsException extends RuntimeException {

    // Konstruktor som tar ett felmeddelande som argument
    public InvalidCredentialsException(String message) {
        // Skickar vidare felmeddelandet till superklassens konstruktor
        super(message);
    }
}

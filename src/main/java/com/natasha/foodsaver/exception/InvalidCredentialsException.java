package com.natasha.foodsaver.exception;

// En exception som kastas när användarens inloggningsuppgifter är ogiltiga
public class InvalidCredentialsException extends RuntimeException {

    // Konstruktor som tar ett felmeddelande som argument
    public InvalidCredentialsException(String message) {
        // Skickar vidare felmeddelandet till superklassens konstruktor
        super(message);
    }
}

package com.natasha.foodsaver.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

// ControllerAdvice används för att fånga undantag (exceptions) globalt för hela applikationen
@ControllerAdvice
public class GlobalExceptionHandler {

    // Hanterar undantag för ogiltig e-postverifiering
    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<ResponseMessage> handleEmailNotVerifiedException(EmailNotVerifiedException e) {
        // Returnerar ett svar med status FORBIDDEN (403) och meddelande om att e-posten inte är verifierad
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ResponseMessage("Email not verified: " + e.getMessage(), null));
    }

    // Hanterar undantag för användare som redan finns
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ResponseMessage> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        // Returnerar ett svar med status CONFLICT (409) och meddelande om att användaren redan finns
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ResponseMessage("User already exists: " + e.getMessage(), null));
    }

    // Hanterar undantag för användare som inte finns
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseMessage> handleUserNotFoundException(UserNotFoundException e) {
        // Returnerar ett svar med status NOT_FOUND (404) och meddelande om att användaren inte hittades
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseMessage("User not found: " + e.getMessage(), null));
    }

    // Hanterar generella RuntimeException-fel
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseMessage> handleRuntimeException(RuntimeException e) {
        // Returnerar ett svar med status INTERNAL_SERVER_ERROR (500) och meddelande om ett systemfel
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseMessage("An error occurred: " + e.getMessage(), null));
    }

    // Hanterar alla andra undantag som inte fångas av de specifika undantagsmetoderna
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        // Skriver ut stacktracen för undantaget i loggen och returnerar ett generellt felmeddelande
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + ex.getMessage());
    }

    // Klass som används för att returnera ett meddelande och eventuellt data i svaret
    @Getter
    public static class ResponseMessage {
        private String message;  // Felmeddelande
        private Object data;     // Eventuell data som kan skickas med svaret

        // Konstruktor för att sätta felmeddelandet och data
        public ResponseMessage(String message, Object data) {
            this.message = message;
            this.data = data;
        }
    }
}

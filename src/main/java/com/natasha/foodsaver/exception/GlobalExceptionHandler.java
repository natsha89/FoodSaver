package com.natasha.foodsaver.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// This class globally handles exceptions that occur in the application
@ControllerAdvice
public class GlobalExceptionHandler {

    // Handles EmailNotVerifiedException and returns a FORBIDDEN (403) response
    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<ResponseMessage> handleEmailNotVerifiedException(EmailNotVerifiedException e) {
        // Return a custom response message with status code FORBIDDEN (403)
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ResponseMessage("Email not verified: " + e.getMessage(), null));
    }

    // Handles UserAlreadyExistsException and returns a CONFLICT (409) response
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ResponseMessage> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        // Return a custom response message with status code CONFLICT (409)
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ResponseMessage("User already exists: " + e.getMessage(), null));
    }

    // Handles UserNotFoundException and returns a NOT_FOUND (404) response
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseMessage> handleUserNotFoundException(UserNotFoundException e) {
        // Return a custom response message with status code NOT_FOUND (404)
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseMessage("User not found: " + e.getMessage(), null));
    }

    // Handles any general RuntimeException and returns an INTERNAL_SERVER_ERROR (500) response
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseMessage> handleRuntimeException(RuntimeException e) {
        // Return a custom response message with status code INTERNAL_SERVER_ERROR (500)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseMessage("An error occurred: " + e.getMessage(), null));
    }

    // Catches any other general exceptions and returns an INTERNAL_SERVER_ERROR (500) response
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        // Log the stack trace for debugging purposes (e.g., for internal monitoring)
        ex.printStackTrace();
        // Return a custom response message with status code INTERNAL_SERVER_ERROR (500)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred: " + ex.getMessage());
    }

    // Custom class for packaging the response message to be returned in the response body
    @Getter
    public static class ResponseMessage {
        private String message;
        private Object data;

        // Constructor to initialize the message and data
        public ResponseMessage(String message, Object data) {
            this.message = message;
            this.data = data;
        }

        // Setter methods (optional)
        public void setMessage(String message) {
            this.message = message;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}

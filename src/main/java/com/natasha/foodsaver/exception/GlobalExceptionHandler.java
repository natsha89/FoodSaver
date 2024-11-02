package com.natasha.foodsaver.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseMessage> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseMessage(ex.getMessage(), null));
    }

    @Getter
    public static class ResponseMessage {
        private String message;
        private Object data;

        public ResponseMessage(String message, Object data) {
            this.message = message;
            this.data = data;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}

package com.natasha.foodsaver;

import com.natasha.foodsaver.controller.AuthController;
import com.natasha.foodsaver.exception.GlobalExceptionHandler;
import com.natasha.foodsaver.exception.UserAlreadyExistsException;
import com.natasha.foodsaver.model.User;
import com.natasha.foodsaver.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("1", "John Doe", "john@example.com", "password123", null, null, null);
    }

    @Test
    public void testRegisterSuccess() {
        when(authService.register(any(User.class))).thenReturn(user);

        ResponseEntity<GlobalExceptionHandler.ResponseMessage> response = authController.register(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User registered successfully!", response.getBody().getMessage());
        assertEquals(user, response.getBody().getData());
        verify(authService, times(1)).register(user);
    }

    @Test
    public void testRegisterUserAlreadyExists() {
        when(authService.register(any(User.class))).thenThrow(new UserAlreadyExistsException("User already exists."));

        ResponseEntity<GlobalExceptionHandler.ResponseMessage> response = authController.register(user);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User already exists.", response.getBody().getMessage());
        verify(authService, times(1)).register(user);
    }

    @Test
    public void testLoginSuccess() {
        when(authService.login("john@example.com", "password123")).thenReturn(user);

        ResponseEntity<GlobalExceptionHandler.ResponseMessage> response = authController.login("john@example.com", "password123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Welcome, John Doe!", response.getBody().getMessage());
        assertEquals(user, response.getBody().getData());
        verify(authService, times(1)).login("john@example.com", "password123");
    }

    @Test
    public void testLoginInvalidCredentials() {
        when(authService.login("john@example.com", "wrongpassword")).thenThrow(new RuntimeException("Invalid email or password."));

        ResponseEntity<GlobalExceptionHandler.ResponseMessage> response = authController.login("john@example.com", "wrongpassword");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid email or password.", response.getBody().getMessage());
        verify(authService, times(1)).login("john@example.com", "wrongpassword");
    }

    @Test
    public void testDeleteAccountSuccess() {
        // Assuming deleteAccount is a void method, use doNothing
        doNothing().when(authService).deleteAccount(String.valueOf(anyLong()));

        ResponseEntity<GlobalExceptionHandler.ResponseMessage> response = authController.deleteAccount(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Your account has been deleted successfully.", response.getBody().getMessage());
        verify(authService, times(1)).deleteAccount(String.valueOf(anyLong()));
    }

    @Test
    public void testDeleteAccountFailure() {
        // If deleteAccount can throw an exception, mock that behavior
        doThrow(new RuntimeException("Error deleting account.")).when(authService).deleteAccount(String.valueOf(anyLong()));

        ResponseEntity<GlobalExceptionHandler.ResponseMessage> response = authController.deleteAccount(user);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error deleting account: Error deleting account.", response.getBody().getMessage());
        verify(authService, times(1)).deleteAccount(String.valueOf(anyLong()));
    }
}

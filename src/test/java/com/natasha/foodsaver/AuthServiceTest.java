package com.natasha.foodsaver;

import com.natasha.foodsaver.exception.UserAlreadyExistsException;
import com.natasha.foodsaver.model.User;
import com.natasha.foodsaver.repository.UserRepository;
import com.natasha.foodsaver.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder; // Använd mock istället för ny instans

    @InjectMocks
    private AuthService authService;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId("1");
        user.setFullName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setSavedRecipes(Arrays.asList("recipe1", "recipe2"));
        user.setAllergies(Arrays.asList("nuts"));
        user.setDietaryPreferences(Arrays.asList("vegan"));
    }

    @Test
    public void testRegisterUserSuccess() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword"); // Mocka encode-metoden
        when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = authService.register(user);

        assertNotNull(registeredUser);
        assertEquals(user.getEmail(), registeredUser.getEmail());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testRegisterUserAlreadyExists() {
        when(userRepository.findByEmail(anyString())).thenReturn(user);

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            authService.register(user);
        });

        assertEquals("Email is already in use.", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testLoginSuccess() {
        String encodedPassword = "encodedPassword";
        user.setPassword(encodedPassword);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true); // Mocka matches-metoden

        User loggedInUser = authService.login(user.getEmail(), "password");

        assertNotNull(loggedInUser);
        assertEquals(user.getEmail(), loggedInUser.getEmail());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    public void testLoginInvalidCredentials() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(user.getEmail(), user.getPassword());
        });

        assertEquals("Invalid credentials.", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    public void testDeleteAccountSuccess() {
        when(userRepository.existsById("1")).thenReturn(true);
        doNothing().when(userRepository).deleteById(anyString());

        assertDoesNotThrow(() -> authService.deleteAccount("1"));

        verify(userRepository, times(1)).deleteById("1");
    }

    @Test
    public void testDeleteAccountNotFound() {
        when(userRepository.existsById("1")).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.deleteAccount("1");
        });

        assertEquals("User not found.", exception.getMessage());
        verify(userRepository, times(1)).existsById("1");
        verify(userRepository, never()).deleteById(anyString());
    }
}

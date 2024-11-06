package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/auth")
public class VerificationController {

    @Autowired
    private AuthService authService;

    @GetMapping("/verify")
    public RedirectView verifyEmail(@RequestParam String token) {
        try {
            boolean verified = authService.verifyEmail(token);
            if (verified) {
                return new RedirectView("http://localhost:8080/login"); // Redirect to login page
            } else {
                return new RedirectView("http://localhost:8080/signup?error=Invalid or expired token"); // Invalid token
            }
        } catch (RuntimeException e) {
            return new RedirectView("http://localhost:8080/signup?error=Error verifying email"); // Error handling
        }
    }
}
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

import java.util.HashMap;
import java.util.Map;

/*
 * MIT License
 * Copyright (c) [2024] [Natasha Shahran]
 *
 * Permission is granted under the MIT License to use, modify, and distribute
 * this software, provided credit is given to the original creator ([Natasha Shahran]).
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 */


@RestController
@RequestMapping("/api/auth")
public class VerificationController {

    @Autowired
    private AuthService authService;

    // Endpoint to verify an email address with a token and redirect accordingly
    @GetMapping("/verify")
    public RedirectView verifyEmail(@RequestParam String token) {
        RedirectView redirectView = new RedirectView();
        try {
            boolean verified = authService.verifyEmail(token);
            if (verified) {
                redirectView.setUrl("http://localhost:3000/verification-success");
            } else {
                redirectView.setUrl("http://localhost:3000/verification-expired");
            }
        } catch (RuntimeException e) {
            redirectView.setUrl("http://localhost:3000/verification-error");
        }
        return redirectView;
    }

    // Endpoint to send a new verification email to the user
    @GetMapping("/resend-verification")
    public ResponseEntity<Map<String, String>> resendVerificationEmail(@RequestParam String email) {
        try {
            authService.resendVerificationEmail(email);
            Map<String, String> response = new HashMap<>();
            response.put("status", "resend");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

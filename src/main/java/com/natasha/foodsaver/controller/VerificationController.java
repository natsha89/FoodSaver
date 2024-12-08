package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class VerificationController {

    @Autowired
    private AuthService authService;  // Injektar AuthService för att hantera autentisering och e-postverifiering

    // Endpoint för att verifiera en e-postadress med en token
    @GetMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyEmail(@RequestParam String token) {
        try {
            // Anropa AuthService för att verifiera e-posten med den angivna token
            boolean verified = authService.verifyEmail(token);
            Map<String, String> response = new HashMap<>();

            // Om e-posten verifierades framgångsrikt, sätt status till "success"
            if (verified) {
                response.put("status", "success");
            } else {
                // Om token har gått ut eller är ogiltig, sätt status till "expired"
                response.put("status", "expired");
            }

            // Returnera ett framgångsrikt svar med statusen
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Vid fel, sätt status till "error" och returnera ett internt serverfel
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Endpoint för att skicka ett nytt verifieringsmail till användaren
    @GetMapping("/resend-verification")
    public ResponseEntity<Map<String, String>> resendVerificationEmail(@RequestParam String email) {
        try {
            // Anropa AuthService för att skicka ett nytt verifieringsmail till den angivna e-posten
            authService.resendVerificationEmail(email);
            Map<String, String> response = new HashMap<>();
            // Om verifieringsmailet skickas, sätt status till "resend"
            response.put("status", "resend");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Vid fel, sätt status till "error" och returnera ett internt serverfel
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

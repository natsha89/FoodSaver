package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.bind.annotation.RestController;

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
                return new RedirectView("http://localhost:8081/verify"); // Omdirigera till verifikationssidan om det är framgångsrikt
            } else {
                return new RedirectView("http://localhost:8081/signup?error=Invalid or expired token"); // Om token är ogiltig
            }
        } catch (RuntimeException e) {
            return new RedirectView("http://localhost:8081/signup?error=Error verifying email"); // Om det uppstår ett fel
        }
    }
}

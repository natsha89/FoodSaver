package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Kontrollera om verifieringen lyckades
    @GetMapping("/verify")
    public RedirectView verifyEmail(@RequestParam String token) {
        try {
            boolean verified = authService.verifyEmail(token);
            if (verified) {
                return new RedirectView("http://localhost:8081/verify?status=success"); // Användare är verifierad
            } else {
                return new RedirectView("http://localhost:8081/verify?status=expired"); // Token har gått ut
            }
        } catch (RuntimeException e) {
            return new RedirectView("http://localhost:8081/verify?status=error"); // Fel vid verifiering
        }
    }

    // Begär en ny verifieringslänk
    @GetMapping("/resend-verification")
    public RedirectView resendVerificationEmail(@RequestParam String email) {
        try {
            authService.resendVerificationEmail(email); // Skicka ny länk
            return new RedirectView("http://localhost:8081/verify?status=resend"); // Begäran om att skicka länken igen
        } catch (RuntimeException e) {
            return new RedirectView("http://localhost:8081/verify?status=error"); // Fel
        }
    }
}

package com.natasha.foodsaver.controller;

import com.natasha.foodsaver.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController  // Denna klass hanterar REST API-anrop och är en controller för verifiering
@RequestMapping("/api/auth")  // Definierar basvägen för alla endpoints relaterade till autentisering och verifiering
public class VerificationController {

    @Autowired
    private AuthService authService;  // Injektionspunkt för tjänsten som hanterar autentisering och verifiering

    // Endpoint för att verifiera en användares e-postadress med hjälp av en verifieringstoken
    @GetMapping("/verify")
    public RedirectView verifyEmail(@RequestParam String token) {
        try {
            boolean verified = authService.verifyEmail(token);  // Anropa authService för att verifiera token
            if (verified) {
                // Om verifieringen lyckades, omdirigera användaren till en success-sida
                return new RedirectView("http://localhost:8081/verify?status=success");
            } else {
                // Om token har gått ut eller är ogiltig, omdirigera användaren till en expired-sida
                return new RedirectView("http://localhost:8081/verify?status=expired");
            }
        } catch (RuntimeException e) {
            // Om något går fel under verifieringen, omdirigera till en error-sida
            return new RedirectView("http://localhost:8081/verify?status=error");
        }
    }

    // Endpoint för att begära en ny verifieringslänk för användarens e-post
    @GetMapping("/resend-verification")
    public RedirectView resendVerificationEmail(@RequestParam String email) {
        try {
            authService.resendVerificationEmail(email);  // Skicka en ny verifieringslänk via AuthService
            // Omdirigera användaren till en sida som visar att länken har skickats
            return new RedirectView("http://localhost:8081/verify?status=resend");
        } catch (RuntimeException e) {
            // Om ett fel uppstår, omdirigera till en error-sida
            return new RedirectView("http://localhost:8081/verify?status=error");
        }
    }
}

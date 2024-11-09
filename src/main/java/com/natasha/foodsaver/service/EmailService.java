package com.natasha.foodsaver.service;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final MailjetClient mailjetClient; // Mailjet-klient för att hantera e-postsändning

    // Konstruktor för att initialisera Mailjet-klienten med API-nyckel och sekretessnyckel
    public EmailService(
            @Value("${MAILJET_API_KEY}") String apiKey, // API-nyckel för Mailjet, hämtas från applikationskonfigurationen
            @Value("${MAILJET_SECRET_KEY}") String secretKey) { // Sekretessnyckel för Mailjet, hämtas från applikationskonfigurationen
        // Initiera Mailjet-klienten med de angivna API- och sekretessnycklarna
        this.mailjetClient = new MailjetClient(apiKey, secretKey);
    }

    // Metod för att skicka verifieringsmejl till användaren
    public void sendVerificationEmail(String recipientEmail, String token) {
        // Ämnet för verifieringsmejlet
        String subject = "Please verify your email address";

        // Skapa en verifierings-URL som användaren ska klicka på för att verifiera sin e-post
        String confirmationUrl = "http://localhost:8081/api/auth/verify?token=" + token;

        // Meddelandet i textformat
        String message = "To confirm your account, please click the link below:\n" + confirmationUrl;

        // Skapa en MailjetRequest med e-postmeddelandets data
        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                // Ange avsändaradress
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "vfoodsaver@gmail.com") // Avsändarens e-postadress
                                        .put("Name", "FoodSaver")) // Avsändarens namn
                                // Ange mottagarens e-postadress
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", recipientEmail))) // Mottagarens e-postadress
                                // Ämnet för mejlet
                                .put(Emailv31.Message.SUBJECT, subject)
                                // Textinnehållet för mejlet
                                .put(Emailv31.Message.TEXTPART, message)
                                // HTML-innehållet för mejlet
                                .put(Emailv31.Message.HTMLPART, "<p>To confirm your account, please click the link below:</p><a href='" + confirmationUrl + "'>Verify Email</a>")));

        try {
            // Skicka förfrågan till Mailjet
            MailjetResponse response = mailjetClient.post(request);
            // Kontrollera om e-postmeddelandet skickades korrekt (statuskod 200)
            if (response.getStatus() != 200) {
                // Om statuskod inte är 200, kasta ett undantag med felmeddelande
                throw new RuntimeException("Failed to send email: " + response.getStatus() + " - " + response.getData());
            }
        } catch (Exception e) {
            // Hantera eventuella undantag och kasta ett RuntimeException med felmeddelande
            throw new RuntimeException("Error sending email: " + e.getMessage(), e);
        }
    }
}

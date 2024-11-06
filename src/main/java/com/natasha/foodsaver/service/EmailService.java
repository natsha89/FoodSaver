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

    private final MailjetClient mailjetClient;

    public EmailService(
            @Value("${MAILJET_API_KEY}") String apiKey,
            @Value("${MAILJET_SECRET_KEY}") String secretKey) {
        // Initialize the Mailjet client with API key and secret key.
        this.mailjetClient = new MailjetClient(apiKey, secretKey);
    }

    public void sendVerificationEmail(String recipientEmail, String token) {
        String subject = "Please verify your email address";
        String confirmationUrl = "http://localhost:8081/api/auth/verify?token=" + token;
        String message = "To confirm your account, please click the link below:\n" + confirmationUrl;

        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "vfoodsaver@gmail.com")
                                        .put("Name", "FoodSaver"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", recipientEmail)))
                                .put(Emailv31.Message.SUBJECT, subject)
                                .put(Emailv31.Message.TEXTPART, message)
                                .put(Emailv31.Message.HTMLPART, "<p>To confirm your account, please click the link below:</p><a href='" + confirmationUrl + "'>Verify Email</a>")));

        try {
            MailjetResponse response = mailjetClient.post(request);
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed to send email: " + response.getStatus() + " - " + response.getData());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error sending email: " + e.getMessage(), e);
        }
    }
}

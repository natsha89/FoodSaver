package com.natasha.foodsaver;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class JwtTest {
    public static void main(String[] args) throws Exception {
        // Din hemliga nyckel (Base64-kodad)
        String secretKey = "qUgdrNxy5c3DPu2ukQO9ViCxwwY1x/F3Z+kLiJpO028=";

        // Exempel på JWT Header och Payload
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payload = "{\"userId\":\"12345\",\"exp\":1710203040}";

        // Base64Url-enkoda Header och Payload
        String encodedHeader = Base64.getUrlEncoder().withoutPadding().encodeToString(header.getBytes());
        String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());

        // Kombinera Header och Payload
        String headerPayload = encodedHeader + "." + encodedPayload;

        // Generera signatur
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(secretKey), "HmacSHA256");
        hmacSha256.init(secretKeySpec);

        byte[] signatureBytes = hmacSha256.doFinal(headerPayload.getBytes());
        String encodedSignature = Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);

        // Bygg JWT
        String jwt = headerPayload + "." + encodedSignature;

        System.out.println("Generated JWT: " + jwt);
    }
}

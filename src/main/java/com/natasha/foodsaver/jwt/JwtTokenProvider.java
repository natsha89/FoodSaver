package com.natasha.foodsaver.jwt;

import com.natasha.foodsaver.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")  // Ladda SECRET_KEY från application.properties
    private String secretKey;

    private static final long EXPIRATION_TIME = 86400000;  // Token expiry time (1 day)

    // Metod för att generera JWT-token
    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(user.getEmail()) // Användarens email eller ID som "subject"
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey) // Signera token med en hemlig nyckel
                .compact();
    }
}

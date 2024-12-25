package com.natasha.foodsaver.service;

import com.natasha.foodsaver.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/*
 * MIT License
 * Copyright (c) [2024] [Natasha Shahran]
 *
 * Permission is granted under the MIT License to use, modify, and distribute
 * this software, provided credit is given to the original creator ([Natasha Shahran]).
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 */

@Service
public class JwtService {

    // Hämtar den hemliga nyckeln (t.ex. från application.properties) för att signera JWT-token
    @Value("${jwt.secret}")
    private String secretKey;

    // Hämtar utgångstiden för token (i millisekunder) från konfigurationsfilen
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    //public String extractUserId(String token) {return extractClaim(token, claims -> claims.get("userId", String.class));}

    public String extractUserIdFromToken(String token) {
        System.out.println("Token: " + token); // Log the token
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody();
        return claims.get("userId", String.class);
    }



    // Extraherar en specifik claim (information) från token genom en funktion som definieras av anroparen
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Genererar en JWT-token för en användare, utan några extra claims
    public String generateToken(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());

        String token = buildToken(extraClaims, user, jwtExpiration);

        System.out.println("Generated Token: " + token);
        System.out.println("User ID in Token: " + user.getId());

        return token;
    }

    // Generera en JWT-token med användarens ID som en custom claim
    public String generateToken(Map<String, Object> extraClaims, User user) {
        // Lägg till användarens ID som en claim
        extraClaims.put("userId", user.getId()); // Lägg till användarens ID i token

        return buildToken(extraClaims, user, jwtExpiration);
    }

    // Hämtar utgångstiden för JWT-token
    public long getExpirationTime() {
        return jwtExpiration;
    }

    // Bygger och signer en JWT-token baserat på användarinformation och extra claims
    private String buildToken(
            Map<String, Object> extraClaims, // Extra data som kan inkluderas i token
            UserDetails userDetails, // Användarens detaljer (t.ex. namn, roller)
            long expiration // Tokenens utgångstid i millisekunder
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims) // Lägg till eventuella extra claims, som userId
                .setSubject(userDetails.getUsername()) // Sätt användarnamnet som "subject"
                .setIssuedAt(new Date(System.currentTimeMillis())) // Sätt utfärdandedatum
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Sätt utgångsdatum baserat på expiration
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Signera token med den hemliga nyckeln och algoritmen HS256
                .compact(); // Skapa den färdiga token
    }


    // Kontrollerar om en JWT-token är giltig genom att jämföra användarnamnet och kolla om den har gått ut
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserIdFromToken(token); // Extrahera användarnamnet från token
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token); // Token är giltig om användarnamnet matchar och token inte har gått ut
    }

    // Kollar om token har gått ut
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // Om utgångsdatumet är tidigare än nuvarande datum, är token utgången
    }

    // Hämtar utgångsdatumet från en given token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extraherar alla claims (data) från en given token
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder() // Bygg en parser
                .setSigningKey(getSignInKey()) // Sätt den hemliga nyckeln för att validera signaturen
                .build()
                .parseClaimsJws(token) // Parsar och validerar JWT-token
                .getBody(); // Hämtar själva kroppen av token, som innehåller alla claims
    }

    // Hämtar den hemliga nyckeln för signering av JWT
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
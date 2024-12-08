package com.natasha.foodsaver.config;

import com.natasha.foodsaver.service.CustomUserDetailsService;
import com.natasha.foodsaver.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Markerar denna klass som en Spring-komponent, så den kan användas som en bean i applikationen
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService; // Tjänst för att hantera JWT-token
    private final CustomUserDetailsService userDetailsService; // Tjänst för att ladda användardata baserat på ID

    // Konstruktor för att injicera JwtService och CustomUserDetailsService
    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Hämta token från begäran
        String token = extractToken(request);

        try {
            // Om en token finns
            if (token != null) {
                // Extrahera användar-ID från token
                String userId = jwtService.extractUserIdFromToken(token);

                // Om användar-ID finns och ingen autentisering har satts i SecurityContext
                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Ladda användardetaljer baserat på användar-ID
                    UserDetails userDetails = userDetailsService.loadUserById(userId);

                    // Om token är giltig för användaren
                    if (jwtService.isTokenValid(token, userDetails)) {
                        // Skapa en autentiseringstoken
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        // Sätt autentiseringen i SecurityContext
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            // Hantera om token har gått ut
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Skicka status 401 (unauthorized)
            response.getWriter().write("Token has expired. Please log in again."); // Skriv felmeddelande
            return; // Stoppa filterkedjan
        } catch (io.jsonwebtoken.MalformedJwtException ex) {
            // Hantera om token är felaktigt formaterad
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Skicka status 400 (bad request)
            response.getWriter().write("Malformed token."); // Skriv felmeddelande
            return; // Stoppa filterkedjan
        } catch (io.jsonwebtoken.SignatureException ex) {
            // Hantera om signaturen för token är ogiltig
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Skicka status 401 (unauthorized)
            response.getWriter().write("Invalid token signature."); // Skriv felmeddelande
            return; // Stoppa filterkedjan
        } catch (Exception ex) {
            // Hantera andra undantag
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Skicka status 500 (internal server error)
            response.getWriter().write("An error occurred while processing the token."); // Skriv felmeddelande
            return; // Stoppa filterkedjan
        }

        // Fortsätt med filterkedjan om ingen undantag inträffade
        filterChain.doFilter(request, response);
    }

    // Extraherar JWT-token från Authorization-headern i begäran
    private String extractToken(HttpServletRequest request) {
        // Hämtar "Authorization"-headern från begäran
        String header = request.getHeader("Authorization");
        // Om headern finns och börjar med "Bearer ", extrahera token
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7); // Ta bort "Bearer " för att få själva token
            System.out.println("Extracted Token: " + token); // För debug, skriv ut token
            return token;
        }
        return null; // Om ingen token hittades, returnera null
    }
}

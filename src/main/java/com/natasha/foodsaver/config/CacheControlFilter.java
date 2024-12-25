

package com.natasha.foodsaver.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;

/*
 * MIT License
 * Copyright (c) [2024] [Natasha Shahran]
 *
 * Permission is granted under the MIT License to use, modify, and distribute
 * this software, provided credit is given to the original creator ([Natasha Shahran]).
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 */

@Component // Markerar denna klass som en Spring-komponent så att den kan användas som en bean i applikationen
public class CacheControlFilter implements Filter {

    @Override
    // Metoden doFilter anropas varje gång en begäran bearbetas genom filtret
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Kastar ServletResponse till HttpServletResponse för att kunna sätta specifika HTTP-headers
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Sätter Cache-Control header för att inaktivera cachelagring på klientsidan
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // Förhindrar caching i alla fall
        httpResponse.setHeader("Pragma", "no-cache"); // För gamla HTTP/1.0-klienter som inte stödjer Cache-Control
        httpResponse.setDateHeader("Expires", 0); // Sätter Expires header till 0 för att signalera att resursen har omedelbart utgått

        // Fortsätter med filterkedjan för att bearbeta den inkommande begäran och generera svaret
        chain.doFilter(request, response);
    }
}

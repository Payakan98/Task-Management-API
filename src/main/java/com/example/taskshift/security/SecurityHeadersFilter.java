package com.example.taskshift.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityHeadersFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Empêche le navigateur de deviner le type MIME
        response.setHeader("X-Content-Type-Options", "nosniff");

        // Empêche l'intégration dans un iframe (clickjacking)
        response.setHeader("X-Frame-Options", "DENY");

        // Force HTTPS pour les 12 prochains mois
        response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");

        // Désactive les anciennes fonctionnalités XSS du navigateur
        response.setHeader("X-XSS-Protection", "0");

        // Contrôle le referrer envoyé dans les requêtes
        response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

        // Restreint les fonctionnalités du navigateur
        response.setHeader("Permissions-Policy", "camera=(), microphone=(), geolocation=()");

        filterChain.doFilter(request, response);
    }
}
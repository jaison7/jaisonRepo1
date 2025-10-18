package com.rag.chat.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Custom filter for API Key Authentication and Rate Limiting.
 * Authentication: Validates 'X-API-KEY' header against a secret environment variable.
 * Rate Limiting: Simple in-memory rate limiter based on the API Key.
 */
@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-KEY";
    private static final int RATE_LIMIT_REQUESTS = 100; // 100 requests
    private static final long RATE_LIMIT_WINDOW_MS = 60_000; // per 1 minute

    @Value("${app.api.key}")
    private String validApiKey;

    // In-memory map for rate limiting: Key -> Last Request Time
    private final ConcurrentMap<String, Long> requestTimestamps = new ConcurrentHashMap<>();

    // In-memory map to store request counts within the window
    private final ConcurrentMap<String, Integer> requestCounts = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Skip security check for public endpoints (Actuator, Swagger)
        String path = request.getRequestURI();
        if (path.startsWith("/actuator") || path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String apiKey = request.getHeader(API_KEY_HEADER);

        if (apiKey == null || apiKey.isEmpty()) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Missing " + API_KEY_HEADER + " header.");
            return;
        }

        if (!apiKey.equals(validApiKey)) {
            sendErrorResponse(response, HttpStatus.FORBIDDEN, "Invalid " + API_KEY_HEADER + ".");
            return;
        }

        // --- Rate Limiting Check (Bonus Requirement) ---
        if (isRateLimited(apiKey)) {
            sendErrorResponse(response, HttpStatus.TOO_MANY_REQUESTS, "Rate limit exceeded. Try again in 60 seconds.");
            return;
        }

        // Authentication Success: Set the Security Context
        // The API Key itself is used as the principal, without any authorities
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                apiKey, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    /**
     * Implements a fixed-window rate limiting strategy.
     */
    private boolean isRateLimited(String apiKey) {
        long now = System.currentTimeMillis();
        
        // Cleanup old entries and reset count if window passed
        requestTimestamps.compute(apiKey, (key, lastTime) -> {
            if (lastTime == null || now - lastTime > RATE_LIMIT_WINDOW_MS) {
                requestCounts.put(key, 1); // Start new window
                return now;
            } else {
                requestCounts.compute(key, (k, count) -> (count == null ? 1 : count + 1));
                return lastTime; // Keep the start time of the current window
            }
        });

        // Check the count against the limit
        return requestCounts.getOrDefault(apiKey, 0) > RATE_LIMIT_REQUESTS;
    }


    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        // Using a simple JSON format for the error response
        response.getWriter().write(String.format("{\"status\":%d, \"error\":\"%s\"}", status.value(), message));
    }
}

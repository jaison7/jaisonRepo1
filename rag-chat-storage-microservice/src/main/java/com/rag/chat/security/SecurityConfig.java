package com.rag.chat.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configures Spring Security for API Key Authentication, CORS, and stateless operation.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ApiKeyAuthFilter apiKeyAuthFilter;

    public SecurityConfig(ApiKeyAuthFilter apiKeyAuthFilter) {
        this.apiKeyAuthFilter = apiKeyAuthFilter;
    }

    /**
     * Configures the main security filter chain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Disable CSRF as this is a stateless REST API
            .csrf(csrf -> csrf.disable())
            
            // 2. Configure CORS (Bonus Requirement)
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.addAllowedOrigin("*"); // Allow all origins for simplicity, but restrict in production
                config.addAllowedMethod("*");
                config.addAllowedHeader("*");
                return config;
            }))

            // 3. Set session management to stateless
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 4. Authorize requests
            .authorizeHttpRequests(auth -> auth
                // Allow public access to actuator health and Swagger/OpenAPI docs
                .requestMatchers("/actuator/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                // All other requests must be authenticated via the API Key filter
                .anyRequest().authenticated()
            )

            // 5. Add custom API Key Authentication Filter
            .addFilterBefore(apiKeyAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

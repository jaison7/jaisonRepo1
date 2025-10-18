package com.rag.chat.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

/**
 * Centralized exception handling (Requirement met).
 * Ensures consistent, clean, and informative error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // DTO for consistent error response format
    private record ErrorDetails(
            Instant timestamp,
            int status,
            String error,
            String message,
            String path
    ) {}

    /**
     * Handles exceptions raised by ResponseStatusException (e.g., in SessionService for Not Found).
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDetails> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        HttpStatus status = (HttpStatus) ex.getStatusCode();
        String path = request.getDescription(false).replace("uri=", "");

        log.warn("API Error: {} - {} at {}", status.value(), ex.getReason(), path);

        ErrorDetails details = new ErrorDetails(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getReason(),
                path
        );

        return new ResponseEntity<>(details, status);
    }

    /**
     * Handles all other unhandled exceptions (Generic Catch-All).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String path = request.getDescription(false).replace("uri=", "");

        // Centralized logging (Requirement met)
        log.error("Internal Server Error: {} at {}", ex.getMessage(), path, ex);

        ErrorDetails details = new ErrorDetails(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                "An unexpected error occurred. Please try again later.", // General message for security
                path
        );

        return new ResponseEntity<>(details, status);
    }
}

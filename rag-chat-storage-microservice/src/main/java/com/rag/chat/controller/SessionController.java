package com.rag.chat.controller;

import com.rag.chat.model.ChatMessage;
import com.rag.chat.model.ChatSession;
import com.rag.chat.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import java.util.List;

/**
 * REST Controller for managing chat sessions and messages.
 * Includes DTOs, Swagger annotations, and validation.
 */
@Tag(name = "Chat Session Management", description = "APIs for creating, managing, and retrieving RAG chat histories.")
@RestController
@RequestMapping("/api/v1/sessions")
@Validated // Enables method parameter validation (e.g., @Min, @NotBlank)
public class SessionController {

    private final SessionService sessionService;

    // Java Record DTOs (Data Transfer Objects) for clean request/response bodies
    // This leverages modern Java features for immutability and conciseness.
    public record CreateSessionRequest(
            @NotBlank(message = "Title is required") String title
    ) {}

    public record RenameSessionRequest(
            @NotBlank(message = "New title is required") String newTitle
    ) {}

    public record ToggleFavoriteRequest(
            @NotNull(message = "Favorite status is required") Boolean isFavorite
    ) {}

    public record AddMessageRequest(
            @NotBlank(message = "Sender is required (e.g., 'user' or 'ai')") String sender,
            @NotBlank(message = "Content is required") String content,
            String retrievedContext // Optional RAG context
    ) {}

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Operation(summary = "Create a new chat session", description = "Starts a new conversation.")
    @PostMapping
    public ResponseEntity<ChatSession> createSession(@RequestBody CreateSessionRequest request) {
        ChatSession newSession = sessionService.createSession(request.title());
        return new ResponseEntity<>(newSession, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all chat sessions", description = "Retrieves a list of all chat sessions for the current user, ordered by last updated.")
    @GetMapping
    public List<ChatSession> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @Operation(summary = "Add a message to a session", description = "Records a new message (user or AI) and optional RAG context in the specified session.")
    @PostMapping("/{sessionId}/messages")
    public ResponseEntity<ChatMessage> addMessage(
            @Parameter(description = "ID of the chat session", required = true) @PathVariable Long sessionId,
            @RequestBody AddMessageRequest request) {

        ChatMessage newMessage = sessionService.addMessage(
                sessionId,
                request.sender(),
                request.content(),
                request.retrievedContext()
        );
        return new ResponseEntity<>(newMessage, HttpStatus.CREATED);
    }

    @Operation(summary = "Get message history with pagination", description = "Retrieves messages for a session, supporting optional pagination.")
    @GetMapping("/{sessionId}/messages")
    public List<ChatMessage> getMessageHistory(
            @Parameter(description = "ID of the chat session", required = true) @PathVariable Long sessionId,
            @Parameter(description = "Page number (0-indexed, default 0)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Size of the page (default 20)") @RequestParam(defaultValue = "20") @Min(1) int size) {
        
        // This method relies on the SessionService, which handles the pagination logic
        return sessionService.getMessageHistory(sessionId, page, size);
    }

    @Operation(summary = "Rename a chat session", description = "Updates the title of an existing chat session.")
    @PatchMapping("/{sessionId}/rename")
    public ChatSession renameSession(
            @Parameter(description = "ID of the chat session", required = true) @PathVariable Long sessionId,
            @RequestBody RenameSessionRequest request) {
        return sessionService.renameSession(sessionId, request.newTitle());
    }

    @Operation(summary = "Toggle favorite status", description = "Marks or unmarks a chat session as a favorite.")
    @PatchMapping("/{sessionId}/favorite")
    public ChatSession toggleFavorite(
            @Parameter(description = "ID of the chat session", required = true) @PathVariable Long sessionId,
            @RequestBody ToggleFavoriteRequest request) {
        return sessionService.toggleFavorite(sessionId, request.isFavorite());
    }

    @Operation(summary = "Delete a chat session", description = "Deletes a session and all associated messages (cascade delete).")
    @DeleteMapping("/{sessionId}")
    @ApiResponse(responseCode = "204", description = "Session successfully deleted", content = @Content(schema = @Schema(hidden = true)))
    public ResponseEntity<Void> deleteSession(
            @Parameter(description = "ID of the chat session", required = true) @PathVariable Long sessionId) {
        sessionService.deleteSession(sessionId);
        return ResponseEntity.noContent().build();
    }
}

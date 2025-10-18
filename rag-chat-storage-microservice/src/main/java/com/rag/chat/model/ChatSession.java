package com.rag.chat.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;

/**
 * Represents a single chat session or conversation.
 * It is the parent entity in the one-to-many relationship with ChatMessage.
 */
@Entity
@Table(name = "chat_sessions")
public class ChatSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User identifier associated with the session (e.g., the RAG system's user ID)
    @Column(nullable = false)
    private String userId;

    // Title of the chat session, required for renaming feature
    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    // Feature requirement: Mark as favorite
    @Column(nullable = false)
    private boolean isFavorite = false;

    // One-to-many relationship with ChatMessage
    // 'cascade = CascadeType.ALL' ensures messages are deleted when the session is deleted (Requirement met)
    // 'orphanRemoval = true' ensures messages are removed if detached from the session
    // 'mappedBy = "session"' indicates the relationship is managed by the 'session' field in ChatMessage
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("id ASC") // Order messages chronologically
    private List<ChatMessage> messages;

    // --- Constructors ---

    public ChatSession() {}

    public ChatSession(String userId, String title) {
        this.userId = userId;
        this.title = title;
    }

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public boolean getIsFavorite() { return isFavorite; }
    public void setIsFavorite(boolean favorite) { isFavorite = favorite; }

    public List<ChatMessage> getMessages() { return messages; }
    public void setMessages(List<ChatMessage> messages) { this.messages = messages; }
}

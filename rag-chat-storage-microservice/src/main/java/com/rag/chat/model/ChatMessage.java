package com.rag.chat.model;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Represents a single message within a chat session.
 * Stores content, sender, and optional RAG context.
 */
@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-one relationship with ChatSession
    // 'fetch = FetchType.LAZY' is efficient for loading messages
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ChatSession session;

    // Sender of the message (e.g., 'user' or 'ai')
    @Column(nullable = false)
    private String sender;

    // The main content of the message
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // Optional context retrieved by the RAG system
    @Column(columnDefinition = "TEXT")
    private String retrievedContext;

    @Column(nullable = false)
    private Instant timestamp = Instant.now();

    // --- Constructors ---

    public ChatMessage() {}

    public ChatMessage(ChatSession session, String sender, String content, String retrievedContext) {
        this.session = session;
        this.sender = sender;
        this.content = content;
        this.retrievedContext = retrievedContext;
    }

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ChatSession getSession() { return session; }
    public void setSession(ChatSession session) { this.session = session; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getRetrievedContext() { return retrievedContext; }
    public void setRetrievedContext(String retrievedContext) { this.retrievedContext = retrievedContext; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}

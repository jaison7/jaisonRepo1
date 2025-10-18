package com.rag.chat.repository;

import com.rag.chat.model.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for ChatSession entities.
 * Provides CRUD and custom querying for session management.
 */
@Repository
public interface SessionRepository extends JpaRepository<ChatSession, Long> {

    /**
     * Finds a session by its ID and ensures it belongs to the specified userId.
     */
    Optional<ChatSession> findByIdAndUserId(Long sessionId, String userId);

    /**
     * Retrieves all sessions for a specific user, ordered by the last updated time.
     */
    List<ChatSession> findByUserIdOrderByUpdatedAtDesc(String userId);

    /**
     * Checks if a session exists for the user.
     */
    boolean existsByIdAndUserId(Long sessionId, String userId);
}

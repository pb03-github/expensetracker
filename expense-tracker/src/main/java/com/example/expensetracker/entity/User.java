package com.example.expensetracker.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

/**
 * User entity - represents a user in the system
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    private UUID userId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public User() {}

    public User(UUID userId, Instant createdAt) {
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

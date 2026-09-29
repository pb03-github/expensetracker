package com.example.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

/**
 * Response DTO for user creation
 */
@Schema(description = "User response")
public record UserResponse(
    @Schema(description = "Unique user identifier", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID userId,
    
    @Schema(description = "User creation timestamp in UTC", example = "2026-09-30T00:30:00Z")
    Instant createdAt
) {}

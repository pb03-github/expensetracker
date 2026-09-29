package com.example.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * Standard error response DTO
 */
@Schema(description = "Error response")
public record ErrorResponse(
    @Schema(description = "Timestamp of the error in UTC", example = "2026-09-30T00:40:00Z")
    Instant timestamp,
    
    @Schema(description = "HTTP status code", example = "400")
    Integer status,
    
    @Schema(description = "Error code", example = "VALIDATION_ERROR")
    String error,
    
    @Schema(description = "Human-readable error message", example = "Amount must be greater than zero")
    String message,
    
    @Schema(description = "Request path", example = "/api/users/.../expenses")
    String path
) {}

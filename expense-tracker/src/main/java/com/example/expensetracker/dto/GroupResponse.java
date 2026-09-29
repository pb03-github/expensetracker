package com.example.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO for group
 */
@Schema(description = "Group response")
public record GroupResponse(
    @Schema(description = "Unique group identifier", example = "1")
    Long groupId,
    
    @Schema(description = "Group name", example = "Personal")
    String name
) {}

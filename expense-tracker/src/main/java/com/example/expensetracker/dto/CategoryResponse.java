package com.example.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO for category
 */
@Schema(description = "Category response")
public record CategoryResponse(
    @Schema(description = "Unique category identifier", example = "1")
    Long categoryId,
    
    @Schema(description = "Category name", example = "Food")
    String name
) {}

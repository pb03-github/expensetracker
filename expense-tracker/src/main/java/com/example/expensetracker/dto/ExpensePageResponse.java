package com.example.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * Paginated response DTO for expense listing
 */
@Schema(description = "Paginated expense list response")
public record ExpensePageResponse(
    @Schema(description = "List of expenses", type = "array")
    List<ExpenseResponse> content,
    
    @Schema(description = "Current page number (zero-indexed)", example = "0")
    Integer page,
    
    @Schema(description = "Page size", example = "20")
    Integer size,
    
    @Schema(description = "Total number of expenses", example = "1")
    Long totalElements,
    
    @Schema(description = "Total number of pages", example = "1")
    Integer totalPages
) {}

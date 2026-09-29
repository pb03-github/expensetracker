package com.example.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Response DTO for expense
 */
@Schema(description = "Expense response")
public record ExpenseResponse(
    @Schema(description = "Unique expense identifier", example = "1001")
    Long expenseId,
    
    @Schema(description = "Expense amount", example = "500.00")
    BigDecimal amount,
    
    @Schema(description = "Associated category", nullable = true)
    CategoryResponse category,
    
    @Schema(description = "Associated group", nullable = true)
    GroupResponse group,
    
    @Schema(description = "Expense creation timestamp in UTC", example = "2026-09-30T00:35:00Z")
    Instant createdAt
) {}

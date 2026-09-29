package com.example.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import java.math.BigDecimal;

/**
 * Request DTO for creating an expense
 */
@Schema(description = "Request to create a new expense")
public record CreateExpenseRequest(
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Digits(integer = 15, fraction = 4, message = "Amount must not exceed 15 integer digits and 4 decimal places")
    @Schema(description = "Expense amount", example = "500.00")
    BigDecimal amount,
    
    @Schema(description = "Optional category identifier", example = "1", nullable = true)
    Long categoryId,
    
    @Schema(description = "Optional group identifier", example = "1", nullable = true)
    Long groupId
) {}

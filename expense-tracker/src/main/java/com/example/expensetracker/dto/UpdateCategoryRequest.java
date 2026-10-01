package com.example.expensetracker.dto;

import com.example.expensetracker.entity.BudgetPeriod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Schema(description = "Request to update a category")
public record UpdateCategoryRequest(
    @NotBlank(message = "Category name must not be blank")
    @Size(max = 100, message = "Category name must not exceed 100 characters")
    @Schema(description = "Category name", example = "Groceries")
    String name,

    @DecimalMin(value = "0.01", message = "Budget must be greater than zero")
    @Digits(integer = 15, fraction = 4, message = "Budget must not exceed 15 integer digits and 4 decimal places")
    @Schema(description = "Optional budget amount", example = "5000.00", nullable = true)
    BigDecimal budgetAmount,

    @Schema(description = "Budget reset period: WEEKLY or MONTHLY", example = "MONTHLY", nullable = true)
    BudgetPeriod budgetPeriod
) {}

package com.example.expensetracker.dto;

import com.example.expensetracker.entity.BudgetPeriod;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Category response")
public record CategoryResponse(
    @Schema(description = "Unique category identifier", example = "1")
    Long categoryId,

    @Schema(description = "Category name", example = "Groceries")
    String name,

    @Schema(description = "Optional budget amount", example = "5000.00", nullable = true)
    BigDecimal budgetAmount,

    @Schema(description = "Budget reset period: WEEKLY or MONTHLY", nullable = true)
    BudgetPeriod budgetPeriod,

    @Schema(description = "Amount spent in the current period", example = "1200.00")
    BigDecimal spentAmount
) {}

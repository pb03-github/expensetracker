package com.example.expensetracker.dto;

import com.example.expensetracker.entity.BudgetPeriod;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Category total in expense summary")
public record CategoryTotalResponse(
    @Schema(description = "Category identifier", example = "1")
    Long categoryId,

    @Schema(description = "Category name", example = "Food")
    String categoryName,

    @Schema(description = "Total amount spent in this category (all time)", example = "5240.00")
    BigDecimal amount,

    @Schema(description = "Budget amount, null if not set", nullable = true)
    BigDecimal budgetAmount,

    @Schema(description = "Budget reset period: WEEKLY or MONTHLY", nullable = true)
    BudgetPeriod budgetPeriod,

    @Schema(description = "Amount spent in the current budget period", example = "1200.00")
    BigDecimal periodSpent
) {}

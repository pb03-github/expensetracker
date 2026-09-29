package com.example.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

/**
 * Response DTO for expense summary
 */
@Schema(description = "Expense summary response")
public record ExpenseSummaryResponse(
    @Schema(description = "Total amount for all expenses", example = "10840.00")
    BigDecimal totalAmount,
    
    @Schema(description = "Breakdown of totals by category", type = "array")
    List<CategoryTotalResponse> categoryTotals,
    
    @Schema(description = "Breakdown of totals by group", type = "array")
    List<GroupTotalResponse> groupTotals
) {}

package com.example.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * Category total DTO for summary
 */
@Schema(description = "Category total in expense summary")
public record CategoryTotalResponse(
    @Schema(description = "Category identifier", example = "1")
    Long categoryId,
    
    @Schema(description = "Category name", example = "Food")
    String categoryName,
    
    @Schema(description = "Total amount for this category", example = "5240.00")
    BigDecimal amount
) {}

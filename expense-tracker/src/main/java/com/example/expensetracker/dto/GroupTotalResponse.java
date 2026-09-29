package com.example.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * Group total DTO for summary
 */
@Schema(description = "Group total in expense summary")
public record GroupTotalResponse(
    @Schema(description = "Group identifier", example = "1")
    Long groupId,
    
    @Schema(description = "Group name", example = "Personal")
    String groupName,
    
    @Schema(description = "Total amount for this group", example = "7500.00")
    BigDecimal amount
) {}

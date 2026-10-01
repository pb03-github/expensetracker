package com.example.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Group member with balance")
public record GroupMemberResponse(
    @Schema(description = "Member name", example = "Alice")
    String name,

    @Schema(description = "Balance: positive means they owe you, negative means you owe them", example = "250.00")
    BigDecimal balance
) {}

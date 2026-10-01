package com.example.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Group response")
public record GroupResponse(
    @Schema(description = "Unique group identifier", example = "1")
    Long groupId,

    @Schema(description = "Group name", example = "Family")
    String name,

    @Schema(description = "Members of this group with their balances")
    List<GroupMemberResponse> members
) {}

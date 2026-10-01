package com.example.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(description = "Request to create a new group")
public record CreateGroupRequest(
    @NotBlank(message = "Group name must not be blank")
    @Size(max = 100, message = "Group name must not exceed 100 characters")
    @Schema(description = "Group name", example = "Family")
    String name,

    @Schema(description = "Optional list of member names to add to the group", nullable = true)
    List<String> memberNames
) {}

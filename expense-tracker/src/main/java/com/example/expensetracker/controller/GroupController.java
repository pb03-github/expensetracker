package com.example.expensetracker.controller;

import com.example.expensetracker.dto.CreateGroupRequest;
import com.example.expensetracker.dto.GroupResponse;
import com.example.expensetracker.dto.UpdateGroupRequest;
import com.example.expensetracker.service.GroupService;
import com.example.expensetracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users/{userId}/groups")
@Tag(name = "Groups", description = "Group management endpoints")
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;

    public GroupController(GroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Create a group", description = "Creates a new group with optional members by name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Group created successfully",
            content = @Content(schema = @Schema(implementation = GroupResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<GroupResponse> createGroup(
        @PathVariable UUID userId,
        @Valid @RequestBody CreateGroupRequest request,
        @RequestHeader(value = "X-User-Name", defaultValue = "You") String creatorName
    ) {
        userService.getUserOrThrow(userId);
        GroupResponse response = groupService.createGroup(userId, creatorName, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{groupId}")
    @Operation(summary = "Update a group", description = "Updates group name and replaces member list")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Group updated successfully",
            content = @Content(schema = @Schema(implementation = GroupResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "404", description = "User or group not found")
    })
    public ResponseEntity<GroupResponse> updateGroup(
        @PathVariable UUID userId,
        @PathVariable Long groupId,
        @Valid @RequestBody UpdateGroupRequest request,
        @RequestHeader(value = "X-User-Name", defaultValue = "You") String creatorName
    ) {
        userService.getUserOrThrow(userId);
        GroupResponse response = groupService.updateGroup(userId, groupId, creatorName, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List user's groups", description = "Returns all groups with members and balances")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Groups retrieved successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = GroupResponse.class)))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<List<GroupResponse>> getGroups(@PathVariable UUID userId) {
        userService.getUserOrThrow(userId);
        List<GroupResponse> groups = groupService.getGroupsForUser(userId);
        return ResponseEntity.ok(groups);
    }
}

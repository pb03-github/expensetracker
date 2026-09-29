package com.example.expensetracker.controller;

import com.example.expensetracker.dto.GroupResponse;
import com.example.expensetracker.service.GroupService;
import com.example.expensetracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for group operations
 */
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
    
    /**
     * Get all groups for a user
     */
    @GetMapping
    @Operation(
        summary = "List user's groups",
        description = "Returns all groups belonging to the user"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Groups retrieved successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = GroupResponse.class)))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found"
        )
    })
    public ResponseEntity<List<GroupResponse>> getGroups(@PathVariable UUID userId) {
        // Verify user exists
        userService.getUserOrThrow(userId);
        
        List<GroupResponse> groups = groupService.getGroupsForUser(userId);
        return ResponseEntity.ok(groups);
    }
}

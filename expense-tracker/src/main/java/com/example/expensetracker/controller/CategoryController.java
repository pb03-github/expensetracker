package com.example.expensetracker.controller;

import com.example.expensetracker.dto.CategoryResponse;
import com.example.expensetracker.service.CategoryService;
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
 * REST Controller for category operations
 */
@RestController
@RequestMapping("/api/users/{userId}/categories")
@Tag(name = "Categories", description = "Category management endpoints")
public class CategoryController {
    
    private final CategoryService categoryService;
    private final UserService userService;
    
    public CategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }
    
    /**
     * Get all categories for a user
     */
    @GetMapping
    @Operation(
        summary = "List user's categories",
        description = "Returns all categories belonging to the user"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Categories retrieved successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoryResponse.class)))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found"
        )
    })
    public ResponseEntity<List<CategoryResponse>> getCategories(@PathVariable UUID userId) {
        // Verify user exists
        userService.getUserOrThrow(userId);
        
        List<CategoryResponse> categories = categoryService.getCategoriesForUser(userId);
        return ResponseEntity.ok(categories);
    }
}

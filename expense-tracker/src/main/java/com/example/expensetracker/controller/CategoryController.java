package com.example.expensetracker.controller;

import com.example.expensetracker.dto.CategoryResponse;
import com.example.expensetracker.dto.CreateCategoryRequest;
import com.example.expensetracker.dto.UpdateCategoryRequest;
import com.example.expensetracker.service.CategoryService;
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
@RequestMapping("/api/users/{userId}/categories")
@Tag(name = "Categories", description = "Category management endpoints")
public class CategoryController {

    private final CategoryService categoryService;
    private final UserService userService;

    public CategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Create a category", description = "Creates a new category with an optional budget")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Category created successfully",
            content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<CategoryResponse> createCategory(
        @PathVariable UUID userId,
        @Valid @RequestBody CreateCategoryRequest request
    ) {
        userService.getUserOrThrow(userId);
        CategoryResponse response = categoryService.createCategory(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "Update a category", description = "Updates name and/or budget of an existing category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category updated successfully",
            content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "404", description = "User or category not found")
    })
    public ResponseEntity<CategoryResponse> updateCategory(
        @PathVariable UUID userId,
        @PathVariable Long categoryId,
        @Valid @RequestBody UpdateCategoryRequest request
    ) {
        userService.getUserOrThrow(userId);
        CategoryResponse response = categoryService.updateCategory(userId, categoryId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List user's categories", description = "Returns all categories with budget and spent amount")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categories retrieved successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoryResponse.class)))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<List<CategoryResponse>> getCategories(@PathVariable UUID userId) {
        userService.getUserOrThrow(userId);
        List<CategoryResponse> categories = categoryService.getCategoriesForUser(userId);
        return ResponseEntity.ok(categories);
    }
}

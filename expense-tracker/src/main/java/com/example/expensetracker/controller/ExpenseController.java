package com.example.expensetracker.controller;

import com.example.expensetracker.dto.*;
import com.example.expensetracker.service.ExpenseService;
import com.example.expensetracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * REST Controller for expense operations
 */
@RestController
@RequestMapping("/api/users/{userId}/expenses")
@Tag(name = "Expenses", description = "Expense management endpoints")
public class ExpenseController {
    
    private final ExpenseService expenseService;
    private final UserService userService;
    
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;
    
    public ExpenseController(ExpenseService expenseService, UserService userService) {
        this.expenseService = expenseService;
        this.userService = userService;
    }
    
    /**
     * Create a new expense
     */
    @PostMapping
    @Operation(
        summary = "Create a new expense",
        description = "Creates a new expense for the user"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Expense created successfully",
            content = @Content(schema = @Schema(implementation = ExpenseResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request body"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User, category, or group not found"
        )
    })
    public ResponseEntity<ExpenseResponse> createExpense(
        @PathVariable UUID userId,
        @Valid @RequestBody CreateExpenseRequest request
    ) {
        // Verify user exists
        userService.getUserOrThrow(userId);
        
        ExpenseResponse response = expenseService.createExpense(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    /**
     * Get paginated expenses with optional filters
     */
    @GetMapping
    @Operation(
        summary = "List user's expenses",
        description = "Returns a paginated list of expenses for the user with optional filters"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Expenses retrieved successfully",
            content = @Content(schema = @Schema(implementation = ExpensePageResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid query parameters"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found"
        )
    })
    public ResponseEntity<ExpensePageResponse> getExpenses(
        @PathVariable UUID userId,
        @Parameter(description = "Filter by category ID")
        @RequestParam(required = false) Long categoryId,
        @Parameter(description = "Filter by group ID")
        @RequestParam(required = false) Long groupId,
        @Parameter(description = "Filter from date (inclusive, UTC)")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @Parameter(description = "Filter to date (inclusive, UTC)")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
        @Parameter(description = "Page number (zero-indexed)")
        @RequestParam(defaultValue = "0") Integer page,
        @Parameter(description = "Page size (1-100)")
        @RequestParam(defaultValue = String.valueOf(DEFAULT_PAGE_SIZE)) Integer size
    ) {
        // Verify user exists
        userService.getUserOrThrow(userId);
        
        // Validate pagination parameters
        if (page < 0) {
            throw new IllegalArgumentException("Page must be non-negative");
        }
        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("Page size must be between 1 and " + MAX_PAGE_SIZE);
        }
        
        // Validate date range
        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("From date must be before or equal to to date");
        }
        
        Pageable pageable = PageRequest.of(page, size);
        ExpensePageResponse response = expenseService.getExpenses(userId, categoryId, groupId, from, to, pageable);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get expense summary
     */
    @GetMapping("/summary")
    @Operation(
        summary = "Get expense summary",
        description = "Returns a summary of all expenses for the user broken down by category and group"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Summary retrieved successfully",
            content = @Content(schema = @Schema(implementation = ExpenseSummaryResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found"
        )
    })
    public ResponseEntity<ExpenseSummaryResponse> getSummary(@PathVariable UUID userId) {
        // Verify user exists
        userService.getUserOrThrow(userId);
        
        ExpenseSummaryResponse response = expenseService.getSummary(userId);
        return ResponseEntity.ok(response);
    }
}

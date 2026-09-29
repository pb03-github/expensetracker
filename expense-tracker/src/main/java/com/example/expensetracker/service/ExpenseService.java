package com.example.expensetracker.service;

import com.example.expensetracker.dto.*;
import com.example.expensetracker.entity.Category;
import com.example.expensetracker.entity.Expense;
import com.example.expensetracker.entity.Group;
import com.example.expensetracker.exception.ResourceNotFoundException;
import com.example.expensetracker.repository.ExpenseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service for expense operations
 */
@Service
@Transactional
public class ExpenseService {
    
    private final ExpenseRepository expenseRepository;
    private final CategoryService categoryService;
    private final GroupService groupService;
    
    public ExpenseService(
        ExpenseRepository expenseRepository,
        CategoryService categoryService,
        GroupService groupService
    ) {
        this.expenseRepository = expenseRepository;
        this.categoryService = categoryService;
        this.groupService = groupService;
    }
    
    /**
     * Create a new expense
     */
    public ExpenseResponse createExpense(UUID userId, CreateExpenseRequest request) {
        Category category = null;
        Group group = null;
        
        // Load and validate category if provided
        if (request.categoryId() != null) {
            category = categoryService.getCategoryOrThrow(userId, request.categoryId());
        }
        
        // Load and validate group if provided
        if (request.groupId() != null) {
            group = groupService.getGroupOrThrow(userId, request.groupId());
        }
        
        // Create expense
        Expense expense = new Expense(
            userId,
            request.amount(),
            request.categoryId(),
            request.groupId(),
            Instant.now()
        );
        
        Expense savedExpense = expenseRepository.save(expense);
        
        return mapToExpenseResponse(savedExpense, category, group);
    }
    
    /**
     * Get paginated expenses with optional filters
     */
    @Transactional(readOnly = true)
    public ExpensePageResponse getExpenses(
        UUID userId,
        Long categoryId,
        Long groupId,
        LocalDate from,
        LocalDate to,
        Pageable pageable
    ) {
        // Convert dates to UTC instants
        Instant fromInstant = null;
        Instant toInstant = null;
        
        if (from != null) {
            fromInstant = from.atStartOfDay(ZoneId.of("UTC")).toInstant();
        }
        
        if (to != null) {
            // Include the entire day - go to start of next day
            toInstant = to.plusDays(1).atStartOfDay(ZoneId.of("UTC")).toInstant();
        }
        
        Page<Expense> expenses = expenseRepository.findExpenses(
            userId,
            categoryId,
            groupId,
            fromInstant,
            toInstant,
            pageable
        );
        
        List<ExpenseResponse> responseList = new ArrayList<>();
        for (Expense expense : expenses.getContent()) {
            Category category = null;
            Group group = null;
            
            if (expense.getCategoryId() != null) {
                try {
                    category = categoryService.getCategoryOrThrow(userId, expense.getCategoryId());
                } catch (ResourceNotFoundException e) {
                    // Category was deleted or doesn't belong to user - skip it
                }
            }
            
            if (expense.getGroupId() != null) {
                try {
                    group = groupService.getGroupOrThrow(userId, expense.getGroupId());
                } catch (ResourceNotFoundException e) {
                    // Group was deleted or doesn't belong to user - skip it
                }
            }
            
            responseList.add(mapToExpenseResponse(expense, category, group));
        }
        
        return new ExpensePageResponse(
            responseList,
            expenses.getNumber(),
            expenses.getSize(),
            expenses.getTotalElements(),
            expenses.getTotalPages()
        );
    }
    
    /**
     * Get expense summary
     */
    @Transactional(readOnly = true)
    public ExpenseSummaryResponse getSummary(UUID userId) {
        BigDecimal totalAmount = expenseRepository.getTotalAmount(userId);
        
        List<CategoryTotalResponse> categoryTotals = new ArrayList<>();
        List<Object[]> categoryResults = expenseRepository.getCategoryTotals(userId);
        for (Object[] row : categoryResults) {
            Long categoryId = ((Number) row[0]).longValue();
            String categoryName = (String) row[1];
            BigDecimal amount = (BigDecimal) row[2];
            categoryTotals.add(new CategoryTotalResponse(categoryId, categoryName, amount));
        }
        
        List<GroupTotalResponse> groupTotals = new ArrayList<>();
        List<Object[]> groupResults = expenseRepository.getGroupTotals(userId);
        for (Object[] row : groupResults) {
            Long groupId = ((Number) row[0]).longValue();
            String groupName = (String) row[1];
            BigDecimal amount = (BigDecimal) row[2];
            groupTotals.add(new GroupTotalResponse(groupId, groupName, amount));
        }
        
        return new ExpenseSummaryResponse(totalAmount, categoryTotals, groupTotals);
    }
    
    /**
     * Map Expense entity to ExpenseResponse DTO
     */
    private ExpenseResponse mapToExpenseResponse(Expense expense, Category category, Group group) {
        CategoryResponse categoryResponse = null;
        if (category != null) {
            categoryResponse = new CategoryResponse(category.getCategoryId(), category.getName());
        }
        
        GroupResponse groupResponse = null;
        if (group != null) {
            groupResponse = new GroupResponse(group.getGroupId(), group.getName());
        }
        
        return new ExpenseResponse(
            expense.getExpenseId(),
            expense.getAmount(),
            categoryResponse,
            groupResponse,
            expense.getCreatedAt()
        );
    }
}

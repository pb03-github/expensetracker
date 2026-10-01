package com.example.expensetracker.service;

import com.example.expensetracker.dto.CategoryResponse;
import com.example.expensetracker.dto.CreateCategoryRequest;
import com.example.expensetracker.dto.UpdateCategoryRequest;
import com.example.expensetracker.entity.BudgetPeriod;
import com.example.expensetracker.entity.Category;
import com.example.expensetracker.exception.ResourceNotFoundException;
import com.example.expensetracker.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse createCategory(UUID userId, CreateCategoryRequest request) {
        Category category = new Category(userId, request.name(), request.budgetAmount(), request.budgetPeriod(), Instant.now());
        Category saved = categoryRepository.save(category);
        return mapToResponse(saved, BigDecimal.ZERO);
    }

    public CategoryResponse updateCategory(UUID userId, Long categoryId, UpdateCategoryRequest request) {
        Category category = getCategoryOrThrow(userId, categoryId);
        category.setName(request.name());
        category.setBudgetAmount(request.budgetAmount());
        category.setBudgetPeriod(request.budgetPeriod());
        Category saved = categoryRepository.save(category);
        BigDecimal spent = getSpentForPeriod(userId, saved);
        return mapToResponse(saved, spent);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategoriesForUser(UUID userId) {
        List<Category> categories = categoryRepository.findByUserIdOrderByNameAscCategoryIdAsc(userId);
        return categories.stream()
            .map(c -> mapToResponse(c, getSpentForPeriod(userId, c)))
            .toList();
    }

    @Transactional(readOnly = true)
    public Category getCategoryOrThrow(UUID userId, Long categoryId) {
        return categoryRepository.findByUserIdAndCategoryId(userId, categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryId));
    }

    private BigDecimal getSpentForPeriod(UUID userId, Category category) {
        return getSpentForPeriodPublic(userId, category);
    }

    public BigDecimal getSpentForPeriodPublic(UUID userId, Category category) {
        if (category.getBudgetPeriod() == null || category.getBudgetAmount() == null) {
            return categoryRepository.getSpentAmount(userId, category.getCategoryId());
        }
        ZoneId zone = ZoneId.of("UTC");
        LocalDate today = LocalDate.now(zone);
        Instant from;
        Instant to = today.plusDays(1).atStartOfDay(zone).toInstant();

        if (category.getBudgetPeriod() == BudgetPeriod.WEEKLY) {
            from = today.with(DayOfWeek.MONDAY).atStartOfDay(zone).toInstant();
        } else {
            from = today.withDayOfMonth(1).atStartOfDay(zone).toInstant();
        }
        return categoryRepository.getSpentAmountInPeriod(userId, category.getCategoryId(), from, to);
    }

    private CategoryResponse mapToResponse(Category c, BigDecimal spent) {
        return new CategoryResponse(c.getCategoryId(), c.getName(), c.getBudgetAmount(), c.getBudgetPeriod(), spent);
    }
}

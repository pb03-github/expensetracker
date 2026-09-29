package com.example.expensetracker.service;

import com.example.expensetracker.dto.CategoryResponse;
import com.example.expensetracker.entity.Category;
import com.example.expensetracker.exception.ResourceNotFoundException;
import com.example.expensetracker.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service for category operations
 */
@Service
@Transactional
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    
    /**
     * Get all categories for a user
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategoriesForUser(UUID userId) {
        List<Category> categories = categoryRepository.findByUserIdOrderByNameAscCategoryIdAsc(userId);
        return categories.stream()
            .map(c -> new CategoryResponse(c.getCategoryId(), c.getName()))
            .toList();
    }
    
    /**
     * Get a category by ID and verify it belongs to the user
     */
    @Transactional(readOnly = true)
    public Category getCategoryOrThrow(UUID userId, Long categoryId) {
        return categoryRepository.findByUserIdAndCategoryId(userId, categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryId));
    }
}

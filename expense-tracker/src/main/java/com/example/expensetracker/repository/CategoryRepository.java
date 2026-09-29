package com.example.expensetracker.repository;

import com.example.expensetracker.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Category entity
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * Find all categories for a specific user
     */
    List<Category> findByUserIdOrderByNameAscCategoryIdAsc(UUID userId);
    
    /**
     * Find a category by ID and user ID
     */
    Optional<Category> findByUserIdAndCategoryId(UUID userId, Long categoryId);
}

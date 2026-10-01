package com.example.expensetracker.repository;

import com.example.expensetracker.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
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

    /**
     * Get total amount spent in a category by a user
     */
    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.userId = :userId AND e.categoryId = :categoryId
    """)
    BigDecimal getSpentAmount(@Param("userId") UUID userId, @Param("categoryId") Long categoryId);

    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.userId = :userId AND e.categoryId = :categoryId
        AND e.createdAt >= :from AND e.createdAt < :to
    """)
    BigDecimal getSpentAmountInPeriod(
        @Param("userId") UUID userId,
        @Param("categoryId") Long categoryId,
        @Param("from") Instant from,
        @Param("to") Instant to
    );
}

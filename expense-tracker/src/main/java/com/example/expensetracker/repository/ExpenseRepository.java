package com.example.expensetracker.repository;

import com.example.expensetracker.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Repository for Expense entity
 */
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    /**
     * Find paginated expenses for a user with optional filters
     */
    @Query("""
        SELECT e FROM Expense e 
        WHERE e.userId = :userId 
        AND (:categoryId IS NULL OR e.categoryId = :categoryId)
        AND (:groupId IS NULL OR e.groupId = :groupId)
        AND (:fromDate IS NULL OR e.createdAt >= :fromDate)
        AND (:toDate IS NULL OR e.createdAt < :toDate)
        ORDER BY e.createdAt DESC, e.expenseId DESC
    """)
    Page<Expense> findExpenses(
        @Param("userId") UUID userId,
        @Param("categoryId") Long categoryId,
        @Param("groupId") Long groupId,
        @Param("fromDate") Instant fromDate,
        @Param("toDate") Instant toDate,
        Pageable pageable
    );
    
    /**
     * Get total amount for all expenses of a user
     */
    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e 
        WHERE e.userId = :userId
    """)
    BigDecimal getTotalAmount(@Param("userId") UUID userId);
    
    /**
     * Get category breakdown for summary
     */
    @Query("""
        SELECT e.categoryId as categoryId, c.name as categoryName, SUM(e.amount) as totalAmount
        FROM Expense e
        LEFT JOIN Category c ON e.categoryId = c.categoryId
        WHERE e.userId = :userId AND e.categoryId IS NOT NULL
        GROUP BY e.categoryId, c.name
        ORDER BY totalAmount DESC, e.categoryId ASC
    """)
    java.util.List<Object[]> getCategoryTotals(@Param("userId") UUID userId);
    
    /**
     * Get group breakdown for summary
     */
    @Query("""
        SELECT e.groupId as groupId, g.name as groupName, SUM(e.amount) as totalAmount
        FROM Expense e
        LEFT JOIN Group g ON e.groupId = g.groupId
        WHERE e.userId = :userId AND e.groupId IS NOT NULL
        GROUP BY e.groupId, g.name
        ORDER BY totalAmount DESC, e.groupId ASC
    """)
    java.util.List<Object[]> getGroupTotals(@Param("userId") UUID userId);
}

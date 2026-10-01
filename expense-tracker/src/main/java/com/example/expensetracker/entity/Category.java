package com.example.expensetracker.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Category entity - represents an expense category owned by a user
 */
@Entity
@Table(name = "categories", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "name"}))
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "budget_amount", precision = 19, scale = 4)
    private BigDecimal budgetAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "budget_period", length = 10)
    private BudgetPeriod budgetPeriod;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public Category() {}

    public Category(UUID userId, String name, BigDecimal budgetAmount, BudgetPeriod budgetPeriod, Instant createdAt) {
        this.userId = userId;
        this.name = name;
        this.budgetAmount = budgetAmount;
        this.budgetPeriod = budgetPeriod;
        this.createdAt = createdAt;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(BigDecimal budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public BudgetPeriod getBudgetPeriod() {
        return budgetPeriod;
    }

    public void setBudgetPeriod(BudgetPeriod budgetPeriod) {
        this.budgetPeriod = budgetPeriod;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

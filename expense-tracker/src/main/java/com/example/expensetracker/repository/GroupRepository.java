package com.example.expensetracker.repository;

import com.example.expensetracker.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Group entity
 */
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    /**
     * Find all groups for a specific user
     */
    List<Group> findByUserIdOrderByNameAscGroupIdAsc(UUID userId);
    
    /**
     * Find a group by ID and user ID
     */
    Optional<Group> findByUserIdAndGroupId(UUID userId, Long groupId);
}

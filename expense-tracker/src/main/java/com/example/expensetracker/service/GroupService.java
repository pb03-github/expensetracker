package com.example.expensetracker.service;

import com.example.expensetracker.dto.GroupResponse;
import com.example.expensetracker.entity.Group;
import com.example.expensetracker.exception.ResourceNotFoundException;
import com.example.expensetracker.repository.GroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service for group operations
 */
@Service
@Transactional
public class GroupService {
    
    private final GroupRepository groupRepository;
    
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }
    
    /**
     * Get all groups for a user
     */
    @Transactional(readOnly = true)
    public List<GroupResponse> getGroupsForUser(UUID userId) {
        List<Group> groups = groupRepository.findByUserIdOrderByNameAscGroupIdAsc(userId);
        return groups.stream()
            .map(g -> new GroupResponse(g.getGroupId(), g.getName()))
            .toList();
    }
    
    /**
     * Get a group by ID and verify it belongs to the user
     */
    @Transactional(readOnly = true)
    public Group getGroupOrThrow(UUID userId, Long groupId) {
        return groupRepository.findByUserIdAndGroupId(userId, groupId)
            .orElseThrow(() -> new ResourceNotFoundException("Group not found: " + groupId));
    }
}

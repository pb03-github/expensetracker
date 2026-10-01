package com.example.expensetracker.service;

import com.example.expensetracker.dto.CreateGroupRequest;
import com.example.expensetracker.dto.GroupMemberResponse;
import com.example.expensetracker.dto.GroupResponse;
import com.example.expensetracker.dto.UpdateGroupRequest;
import com.example.expensetracker.entity.Group;
import com.example.expensetracker.entity.GroupMember;
import com.example.expensetracker.exception.ResourceNotFoundException;
import com.example.expensetracker.repository.ExpenseRepository;
import com.example.expensetracker.repository.GroupMemberRepository;
import com.example.expensetracker.repository.GroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ExpenseRepository expenseRepository;

    public GroupService(GroupRepository groupRepository, GroupMemberRepository groupMemberRepository, ExpenseRepository expenseRepository) {
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.expenseRepository = expenseRepository;
    }

    public GroupResponse createGroup(UUID userId, String creatorName, CreateGroupRequest request) {
        Group group = new Group(userId, request.name(), Instant.now());
        Group saved = groupRepository.save(group);

        Instant now = Instant.now();
        // Add creator as a member with their name
        groupMemberRepository.save(new GroupMember(saved.getGroupId(), userId, creatorName, now));

        List<GroupMemberResponse> members = new ArrayList<>();
        members.add(new GroupMemberResponse(creatorName, BigDecimal.ZERO));

        // Add additional members by name (generate a placeholder UUID per name)
        if (request.memberNames() != null) {
            for (String memberName : request.memberNames()) {
                String trimmed = memberName.trim();
                if (!trimmed.isEmpty()) {
                    UUID memberId = UUID.randomUUID();
                    groupMemberRepository.save(new GroupMember(saved.getGroupId(), memberId, trimmed, now));
                    members.add(new GroupMemberResponse(trimmed, BigDecimal.ZERO));
                }
            }
        }

        return new GroupResponse(saved.getGroupId(), saved.getName(), members);
    }

    @Transactional(readOnly = true)
    public List<GroupResponse> getGroupsForUser(UUID userId) {
        List<Group> groups = groupRepository.findByUserIdOrderByNameAscGroupIdAsc(userId);
        return groups.stream()
            .map(g -> buildGroupResponse(g, userId))
            .toList();
    }

    public GroupResponse updateGroup(UUID userId, Long groupId, String creatorName, UpdateGroupRequest request) {
        Group group = getGroupOrThrow(userId, groupId);
        group.setName(request.name());
        groupRepository.save(group);

        // Replace all non-owner members
        List<GroupMember> existing = groupMemberRepository.findByIdGroupId(groupId);
        for (GroupMember m : existing) {
            if (!m.getId().getUserId().equals(userId)) {
                groupMemberRepository.delete(m);
            } else {
                // Update creator name
                m.setMemberName(creatorName);
                groupMemberRepository.save(m);
            }
        }

        Instant now = Instant.now();
        if (request.memberNames() != null) {
            for (String memberName : request.memberNames()) {
                String trimmed = memberName.trim();
                if (!trimmed.isEmpty()) {
                    groupMemberRepository.save(new GroupMember(groupId, UUID.randomUUID(), trimmed, now));
                }
            }
        }

        return buildGroupResponse(group, userId);
    }

    @Transactional(readOnly = true)
    public Group getGroupOrThrow(UUID userId, Long groupId) {
        return groupRepository.findByUserIdAndGroupId(userId, groupId)
            .orElseThrow(() -> new ResourceNotFoundException("Group not found: " + groupId));
    }

    private GroupResponse buildGroupResponse(Group group, UUID ownerId) {
        List<GroupMember> memberEntities = groupMemberRepository.findByIdGroupId(group.getGroupId());

        // Total spent by owner in this group
        BigDecimal totalSpent = expenseRepository.getTotalAmountByGroup(ownerId, group.getGroupId());
        if (totalSpent == null) totalSpent = BigDecimal.ZERO;

        int memberCount = memberEntities.size();

        // Each member's share = totalSpent / memberCount
        // Balance per non-owner member = their share (they owe you that much)
        // Owner's own share is excluded from balances shown
        List<GroupMemberResponse> members = new ArrayList<>();
        for (GroupMember m : memberEntities) {
            boolean isOwner = m.getId().getUserId().equals(ownerId);
            String name = m.getMemberName() != null ? m.getMemberName() : m.getId().getUserId().toString();
            if (isOwner) {
                // Show owner with zero balance (it's "you")
                members.add(new GroupMemberResponse(name + " (you)", BigDecimal.ZERO));
            } else {
                // Each other member owes their share
                BigDecimal share = memberCount > 0
                    ? totalSpent.divide(BigDecimal.valueOf(memberCount), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;
                members.add(new GroupMemberResponse(name, share));
            }
        }

        return new GroupResponse(group.getGroupId(), group.getName(), members);
    }
}

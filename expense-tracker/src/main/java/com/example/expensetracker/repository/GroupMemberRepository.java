package com.example.expensetracker.repository;

import com.example.expensetracker.entity.GroupMember;
import com.example.expensetracker.entity.GroupMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId> {
    List<GroupMember> findByIdGroupId(Long groupId);
}

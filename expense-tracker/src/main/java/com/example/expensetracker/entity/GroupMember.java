package com.example.expensetracker.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "group_members")
public class GroupMember {

    @EmbeddedId
    private GroupMemberId id;

    @Column(name = "joined_at", nullable = false)
    private Instant joinedAt;

    @Column(name = "member_name", length = 100)
    private String memberName;

    public GroupMember() {}

    public GroupMember(Long groupId, UUID userId, String memberName, Instant joinedAt) {
        this.id = new GroupMemberId(groupId, userId);
        this.memberName = memberName;
        this.joinedAt = joinedAt;
    }

    public GroupMemberId getId() { return id; }
    public void setId(GroupMemberId id) { this.id = id; }
    public Instant getJoinedAt() { return joinedAt; }
    public void setJoinedAt(Instant joinedAt) { this.joinedAt = joinedAt; }
    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }
}

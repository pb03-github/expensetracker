-- V4__Drop_group_members_user_fk.sql
ALTER TABLE group_members DROP CONSTRAINT IF EXISTS fk_group_members_user;

-- V1__Create_initial_schema.sql
-- Initial database schema for Expense Tracker

-- Create users table
CREATE TABLE users (
    user_id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Create categories table
CREATE TABLE categories (
    category_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_categories_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE RESTRICT,
    CONSTRAINT uk_categories_user_name UNIQUE (user_id, name)
);

CREATE INDEX idx_categories_user_id ON categories(user_id);

-- Create groups table
CREATE TABLE groups (
    group_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_groups_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE RESTRICT,
    CONSTRAINT uk_groups_user_name UNIQUE (user_id, name)
);

CREATE INDEX idx_groups_user_id ON groups(user_id);

-- Create expenses table
CREATE TABLE expenses (
    expense_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id UUID NOT NULL,
    amount NUMERIC(19,4) NOT NULL,
    category_id BIGINT,
    group_id BIGINT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_expenses_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE RESTRICT,
    CONSTRAINT fk_expenses_category FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE RESTRICT,
    CONSTRAINT fk_expenses_group FOREIGN KEY (group_id) REFERENCES groups(group_id) ON DELETE RESTRICT,
    CONSTRAINT ck_expenses_amount_positive CHECK (amount > 0)
);

CREATE INDEX idx_expenses_user_created ON expenses(user_id, created_at DESC, expense_id DESC);
CREATE INDEX idx_expenses_user_category ON expenses(user_id, category_id);
CREATE INDEX idx_expenses_user_group ON expenses(user_id, group_id);

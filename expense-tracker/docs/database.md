# Database contract

PostgreSQL only for V1. Flyway creates the schema; `spring.jpa.hibernate.ddl-auto=validate`. Use `TIMESTAMP WITH TIME ZONE` / Java `Instant`, and `NUMERIC(19,4)` / Java `BigDecimal`. Server sets creation timestamps; clients cannot override them.

| Table | Columns | Constraints |
| --- | --- | --- |
| `users` | `user_id UUID`, `created_at TIMESTAMPTZ` | UUID PK, timestamp NOT NULL |
| `categories` | `category_id BIGINT IDENTITY`, `user_id UUID`, `name VARCHAR(100)`, `created_at TIMESTAMPTZ` | PK, user FK, NOT NULL fields, `UNIQUE(user_id, name)`, nonblank trimmed name |
| `groups` | `group_id BIGINT IDENTITY`, `user_id UUID`, `name VARCHAR(100)`, `created_at TIMESTAMPTZ` | PK, user FK, NOT NULL fields, `UNIQUE(user_id, name)`, nonblank trimmed name |
| `expenses` | `expense_id BIGINT IDENTITY`, `user_id UUID`, `amount NUMERIC(19,4)`, `category_id BIGINT NULL`, `group_id BIGINT NULL`, `created_at TIMESTAMPTZ` | PK, user/category/group FKs, amount > 0, timestamp NOT NULL |

The create-user request contains `name`, but the proposed users table does not; resolve [D1](decisions.md) before its migration. At present both expense associations are nullable. FK constraints ensure referenced rows exist, but ordinary single-column FKs **do not** ensure the category/group belongs to the same user as the expense. The service must check both associations; see [security](security.md).

Indexes: `categories(user_id)`, `groups(user_id)`, `expenses(user_id, created_at DESC, expense_id DESC)`, `expenses(user_id, category_id)`, `expenses(user_id, group_id)`. User-scoped uniqueness also supplies a category/group user index; avoid duplicating an equivalent index if the PostgreSQL query plan uses the unique index. Add only new, versioned migrations; never edit an applied migration. A first migration can create all four tables and indexes, or ordered migrations can create users, categories, groups, expenses, then indexes.

Use `ON DELETE RESTRICT`/default FK behavior for referenced rows until deletion semantics are designed. Do not introduce provider-specific hosted database features. Avoid bidirectional collections just to represent the ER diagram.
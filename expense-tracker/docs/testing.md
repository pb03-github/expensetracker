# Backend verification plan

- Unit: expense service accepts category/group/both/neither; rejects nonpositive or missing amount, nonexistent user/category/group, and foreign-owned associations. Category/group services, when implemented, enforce scoping and name rules.
- API: create user, list categories/groups, create expense, filter/paginate list, summary; assert DTO shapes, 201/200, validation 400, missing/foreign reference 404, and standardized errors.
- PostgreSQL integration (prefer Testcontainers if Docker is available): apply Flyway from empty DB; check FKs, unique per-user names, positive amount check, nullable associations, indexes, newest-first tie ordering, filtered pages, aggregates and transaction rollback.
- Performance: prove expense page response does not cause one category/group query per expense; use query-count assertions or SQL inspection where practical.

Do not substitute H2 for PostgreSQL when validating SQL migrations or constraints. If Docker is unavailable, run unit/API tests and clearly mark PostgreSQL integration unverified.
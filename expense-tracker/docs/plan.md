# Backend delivery plan

1. Resolve the [open contract decisions](decisions.md) for user name and category/group maintenance; choose supported Spring Boot version.
2. Scaffold a Maven Java 21 Spring Boot backend; configure PostgreSQL credentials, Flyway and Hibernate validation.
3. Add schema migrations, JPA entities and user-scoped repositories; confirm schema and constraints on PostgreSQL.
4. Add user creation and category/group read APIs (and create APIs if the maintenance decision requires them).
5. Add transactional expense creation with ownership checks, validation, DTOs and consistent errors.
6. Add filtered, bounded, paginated expense listing and database-aggregated summary.
7. Add service, controller and PostgreSQL integration tests; package the executable jar.

V1 done when all six initial endpoints work; scoped data and constraints hold; Flyway owns schema; expenses filter/page/summarize; errors are consistent; tests pass; credentials are external. Public deployment is blocked on authentication and authorization. React and future CRUD, budgets, payment methods and recurring expenses are deferred.
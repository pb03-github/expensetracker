# Backend architecture

One Spring Boot application: controller -> service -> repository -> PostgreSQL. Flyway owns schema changes; Hibernate validates mappings. No microservices, queue, cache, or separate read model.

Suggested source layout under `src/main/java/com/example/expensetracker/`:

```text
controller/     REST endpoints and request validation
service/        user-scoped business rules and transactions
repository/     Spring Data JPA queries and aggregates
entity/         persistence model; never returned by controllers
dto/            request and response records
exception/      domain errors and @RestControllerAdvice
```

Use constructor injection, `BigDecimal` for amount, `Instant` for timestamps, and request/response records. Keep associations from expense to user/category/group unidirectional unless a real query requires a reverse relationship. Use projections or fetch joins for expense list responses to avoid N+1. Aggregate summary amounts in SQL/JPQL, not in memory. Pagination belongs at the repository boundary.

Client-supplied path `userId` is temporary local-development identity, not authentication. Services take a resolved user ID so a later authentication layer can provide it without changing persistence rules. See [security](security.md).
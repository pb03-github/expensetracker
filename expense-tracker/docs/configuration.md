# Runtime and configuration

Maven + Java 21. Spring dependencies: Web, Data JPA, Validation, Flyway, PostgreSQL driver, Test, and Flyway's PostgreSQL support module when required by the selected Boot/Flyway version. Pin a compatible, supported Spring Boot release when the project is scaffolded.

Configuration reads `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` from the environment. `DB_URL` is a JDBC URL such as `jdbc:postgresql://localhost:5432/expense_tracker`; do not check credentials into source. Locally, supply these through a non-committed environment or shell. Startup should run Flyway, then validate Hibernate mappings. Do not enable automatic schema creation.

Suggested build/run workflow once source exists:

```text
Create a local PostgreSQL database and set DB_URL, DB_USERNAME, DB_PASSWORD.
Run ./mvnw test (or mvn test if no wrapper exists).
Run ./mvnw spring-boot:run.
Package with ./mvnw package and deploy the resulting executable jar.
```

These commands are planned, not yet runnable: this folder currently contains documentation only. Hosted backend/PostgreSQL can be configured later without provider-specific SQL. Never publish a path-identity-only version; see [security](security.md).
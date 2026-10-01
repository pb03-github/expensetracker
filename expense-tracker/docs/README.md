# Backend context index

Read this file first, then only the topic files needed for the task. Keep each file short and update the relevant contract when implementation decisions change; do not paste the entire original specification into future prompts.

| Topic | Read when |
| --- | --- |
| [Architecture](architecture.md) | Choosing modules, dependencies, or DTO boundaries |
| [Database](database.md) | Writing migrations, entities, indexes, or queries |
| [API contracts](api/README.md) | Implementing an endpoint or changing a response |
| [Ownership and security](security.md) | Handling user IDs, references, or deployment access |
| [Configuration](configuration.md) | Running locally or packaging the service |
| [Testing](testing.md) | Adding unit, API, or PostgreSQL integration coverage |
| [Delivery plan](plan.md) | Picking the next implementation slice |
| [Decisions](decisions.md) | Resolving ambiguities before code is written |
| [Progress](progress.md) | Resuming after a break |

V1: Java 21, Spring Boot, Maven, Spring Web, Data JPA/Hibernate, PostgreSQL, Flyway, Jakarta Validation, JUnit 5 and Mockito. The browser UI is served from Spring Boot static resources and has no separate frontend build. Authentication is not implemented and is required before any public deployment.
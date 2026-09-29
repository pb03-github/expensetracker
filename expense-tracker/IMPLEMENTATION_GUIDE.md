# Expense Tracker Backend - Implementation Guide

This is a complete implementation of the Expense Tracker backend API using Spring Boot 3.3.5 and Java 21.

## Project Structure

```
src/main/java/com/example/expensetracker/
├── controller/           REST endpoints
├── service/              Business logic
├── repository/           Data access layer
├── entity/               JPA entities
├── dto/                  Request/Response DTOs
├── exception/            Exception handling
└── ExpenseTrackerApplication.java   Main application

src/main/resources/
├── application.yml       Configuration
├── db/migration/         Flyway SQL migrations
└── static/               Static resources (Swagger HTML)
```

## Getting Started

### Prerequisites
- Java 21 or later
- Maven 3.6+
- PostgreSQL 12+ (optional - H2 is configured for local development)

### Local Development (Using H2)

The application is configured to use H2 in-memory database by default for local development.

1. **Clone or navigate to the project directory**
   ```bash
   cd expense-tracker
   ```

2. **Build the project**
   ```bash
   ./mvnw clean package
   ```

3. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access the application**
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - API Docs (JSON): http://localhost:8080/v3/api-docs
   - H2 Console (for debugging): http://localhost:8080/h2-console

### Running Tests

```bash
./mvnw test
```

## API Endpoints

### Users
- `POST /api/users` - Create a new user

### Categories
- `GET /api/users/{userId}/categories` - List user's categories

### Groups
- `GET /api/users/{userId}/groups` - List user's groups

### Expenses
- `POST /api/users/{userId}/expenses` - Create a new expense
- `GET /api/users/{userId}/expenses` - List expenses (with filters and pagination)
- `GET /api/users/{userId}/expenses/summary` - Get expense summary

## Configuration

### Using H2 (Default - Local Development)

The `application.yml` is pre-configured to use H2 in-memory database:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:expense_tracker;MODE=PostgreSQL
    driverClassName: org.h2.Driver
    username: sa
    password: ''
```

### Switching to PostgreSQL

1. **Create a PostgreSQL database**
   ```sql
   CREATE DATABASE expense_tracker;
   ```

2. **Update `application.yml`**
   Uncomment the PostgreSQL section and comment out the H2 section:
   ```yaml
   spring:
     datasource:
       url: ${DB_URL:jdbc:postgresql://localhost:5432/expense_tracker}
       username: ${DB_USERNAME:postgres}
       password: ${DB_PASSWORD:password}
   ```

3. **Set environment variables**
   ```bash
   export DB_URL=jdbc:postgresql://localhost:5432/expense_tracker
   export DB_USERNAME=postgres
   export DB_PASSWORD=your_password
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

## Database Schema

The Flyway migration `V1__Create_initial_schema.sql` creates the following tables:

- `users` - User records
- `categories` - Expense categories (user-scoped)
- `groups` - Expense groups (user-scoped)
- `expenses` - Expense records

### Constraints
- All categories and groups are user-scoped (unique per user)
- Expense amounts must be positive
- Foreign keys enforce referential integrity
- Indexes optimize filtering and pagination

## Testing with Swagger UI

1. Open http://localhost:8080/swagger-ui.html
2. All endpoints are documented with:
   - Request/response examples
   - Parameter descriptions
   - Status codes and error responses
3. Use the "Try it out" button to test endpoints directly

## Example API Workflow

### 1. Create a User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "John Doe"}'
```

Response:
```json
{
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "createdAt": "2026-09-30T00:30:00Z"
}
```

### 2. List Categories
```bash
curl http://localhost:8080/api/users/550e8400-e29b-41d4-a716-446655440000/categories
```

Response:
```json
[
  { "categoryId": 1, "name": "Food" },
  { "categoryId": 2, "name": "Travel" }
]
```

### 3. Create an Expense
```bash
curl -X POST http://localhost:8080/api/users/550e8400-e29b-41d4-a716-446655440000/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 500.00,
    "categoryId": 1,
    "groupId": null
  }'
```

### 4. List Expenses with Filters
```bash
curl "http://localhost:8080/api/users/550e8400-e29b-41d4-a716-446655440000/expenses?categoryId=1&page=0&size=20"
```

### 5. Get Summary
```bash
curl http://localhost:8080/api/users/550e8400-e29b-41d4-a716-446655440000/expenses/summary
```

## Error Handling

All errors follow a consistent format:

```json
{
  "timestamp": "2026-09-30T00:40:00Z",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "Amount must be greater than zero",
  "path": "/api/users/.../expenses"
}
```

## Important Notes

1. **User Identity**: The `userId` in the URL is a temporary local-development identity, not authentication. Each path parameter is independently validated.

2. **Data Scoping**: The service layer ensures that:
   - Categories and groups are looked up by both ID and user ID
   - Expenses can only be associated with user-owned categories/groups
   - Foreign user IDs in requests return 404 to avoid information leaks

3. **Transactions**: All write operations are transactional and atomic

4. **Pagination**: 
   - Default page size: 20
   - Maximum page size: 100
   - Page numbering is zero-indexed

5. **Timestamps**: All timestamps are in UTC and use `java.time.Instant`

6. **Amounts**: Use `BigDecimal` with 4 decimal places precision

## Security Considerations

⚠️ **IMPORTANT**: This implementation is for local development only. Before deployment:

1. Implement proper authentication (OAuth2, JWT, etc.)
2. Add authorization layer to verify user ownership
3. Never use userId as the only identity verification
4. Disable H2 console in production
5. Use environment variables for all credentials
6. Implement rate limiting and CORS policies
7. Add request logging and monitoring
8. Use HTTPS only in production

## Development

### Adding a New Endpoint

1. Create a method in the appropriate service class
2. Add request/response DTOs if needed
3. Add a method in the controller with `@PostMapping`, `@GetMapping`, etc.
4. Add Swagger annotations (`@Operation`, `@ApiResponse`, etc.)
5. Test with Swagger UI

### Modifying the Database Schema

1. Create a new Flyway migration file: `src/main/resources/db/migration/V{N}__Description.sql`
2. Follow naming convention: `V{number}__{description}.sql`
3. Always use `ON DELETE RESTRICT` for foreign keys unless deletion semantics are designed
4. Run the application - Flyway will execute migrations on startup

## Troubleshooting

### H2 Console
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:expense_tracker;MODE=PostgreSQL`
- Username: `sa`
- Password: (leave empty)

### Flyway Issues
- Check migration files for SQL syntax errors
- Ensure all migrations follow naming convention
- Never edit applied migrations
- Check Flyway history: `flyway_schema_history` table

### Port Already in Use
```bash
# Change port in application.yml
server:
  port: 8081
```

## Future Enhancements (Out of Scope for V1)

- POST/PUT/DELETE endpoints for categories and groups maintenance
- React frontend client
- Budget tracking and alerts
- Payment methods management
- Recurring expenses
- User authentication and authorization
- Multi-currency support
- Mobile app
- Data export functionality

## License

MIT License - See LICENSE file for details

## Support

For issues or questions, please check the documentation in `docs/` directory or review the API specs.

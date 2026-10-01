# Expense Tracker

Personal expense tracker built as a Java 21 / Spring Boot modular monolith backed by PostgreSQL (or H2 for local development). A lightweight browser UI is served by Spring Boot; no separate frontend build is required.

## Status: ✅ IMPLEMENTATION COMPLETE

All core functionality has been implemented and is ready for testing:
- ✅ Spring Boot REST API (Java 21)
- ✅ Browser dashboard for expense entry, history, filters, and summaries
- ✅ JPA/Hibernate with Flyway migrations
- ✅ All 6 required endpoints
- ✅ Swagger/OpenAPI documentation
- ✅ Global exception handling
- ✅ Input validation
- ✅ Pagination and filtering
- ✅ Database aggregation for summaries

## 🚀 Quick Start

```bash
# Build and run
./mvnw clean spring-boot:run

# Open Swagger UI
http://localhost:8080/swagger-ui.html

# Open the expense tracker
http://localhost:8080/
```

## 📚 Documentation

- **[Quick Start Guide](QUICK_START.md)** - Get started in 30 seconds
- **[Implementation Guide](IMPLEMENTATION_GUIDE.md)** - Complete setup and configuration
- **[API Specifications](docs/api/README.md)** - Detailed endpoint contracts
- **[Architecture](docs/architecture.md)** - System design decisions
- **[Database Schema](docs/database.md)** - Database structure and constraints

## 🔑 Key Features

- User management with UUID-based identity
- Category and group management (user-scoped)
- Expense creation, listing, and summary
- Filtering by category, group, and date range
- Pagination with configurable page size
- Transaction support for data consistency
- H2 in-memory database (local development)
- PostgreSQL support (production)
- OpenAPI/Swagger documentation
- Comprehensive error handling

## 📋 API Endpoints

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/users` | Create a new user |
| GET | `/api/users/{userId}/categories` | List user's categories |
| GET | `/api/users/{userId}/groups` | List user's groups |
| POST | `/api/users/{userId}/expenses` | Create an expense |
| GET | `/api/users/{userId}/expenses` | List expenses (with filters) |
| GET | `/api/users/{userId}/expenses/summary` | Get expense summary |

## 🛠️ Tech Stack

- **Runtime**: Java 21
- **Framework**: Spring Boot 3.3.5
- **ORM**: Hibernate + JPA
- **Database**: H2 (dev) / PostgreSQL (prod)
- **Migrations**: Flyway
- **Documentation**: Springdoc OpenAPI
- **Build**: Maven

## 📁 Project Structure

```
expense-tracker/
├── pom.xml                                 Maven configuration
├── src/main/
│   ├── java/com/example/expensetracker/
│   │   ├── controller/                     REST endpoints
│   │   ├── service/                        Business logic
│   │   ├── repository/                     Data access
│   │   ├── entity/                         JPA entities
│   │   ├── dto/                            Request/Response DTOs
│   │   ├── exception/                      Exception handling
│   │   ├── ExpenseTrackerApplication.java  Main application
│   │   └── TestDataLoader.java             Optional test data loader
│   └── resources/
│       ├── application.yml                 Configuration
│       ├── db/migration/                   Flyway SQL migrations
│       └── static/swagger.html             Swagger UI
├── docs/                                   Original specifications
├── QUICK_START.md                          Quick start guide
├── IMPLEMENTATION_GUIDE.md                 Complete guide
├── setup.sh                                Setup script
└── .gitignore                              Git ignore rules
```

## 🧪 Testing

All endpoints are documented and testable via:
1. **Swagger UI**: http://localhost:8080/swagger-ui.html
2. **OpenAPI JSON**: http://localhost:8080/v3/api-docs
3. **H2 Console**: http://localhost:8080/h2-console (for debugging)
4. **cURL/Postman**: Direct HTTP requests

See [Quick Start Guide](QUICK_START.md) for detailed testing workflow.

## ⚙️ Configuration

### Local Development (H2)
- Database: In-memory H2
- No external setup required
- Data resets on application restart
- Console available at: http://localhost:8080/h2-console

### Production (PostgreSQL)
1. Update `application.yml` with PostgreSQL config
2. Set environment variables: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
3. Flyway will automatically run migrations on startup

See [Implementation Guide](IMPLEMENTATION_GUIDE.md) for detailed setup.

## 🔒 Security Notes

⚠️ **IMPORTANT**: This implementation is for local development. Before production:
- Implement authentication (OAuth2/JWT)
- Add authorization layer
- Disable H2 console
- Use HTTPS
- Implement rate limiting
- Add comprehensive logging

## 📋 Checklist

- [x] Maven project setup with Spring Boot 3.3.5
- [x] Configuration (H2 + PostgreSQL support)
- [x] Flyway database migrations
- [x] JPA entities (User, Category, Group, Expense)
- [x] DTOs for all requests/responses
- [x] Spring Data JPA repositories
- [x] Service layer with business logic
- [x] REST controllers with validation
- [x] Global exception handling
- [x] OpenAPI/Swagger configuration
- [x] Swagger HTML UI
- [x] Documentation and guides
- [x] Setup script

## 🚀 Next Steps

1. **Run the application**: `./mvnw clean spring-boot:run`
2. **Test via Swagger**: http://localhost:8080/swagger-ui.html
3. **Read guides**: Start with [QUICK_START.md](QUICK_START.md)
4. **Implement features**: Categories/groups POST endpoints (Phase 2)
5. **Deploy**: Switch to PostgreSQL and deploy to production

## 📞 Support

For detailed information, refer to:
- [QUICK_START.md](QUICK_START.md) - Fast testing guide
- [IMPLEMENTATION_GUIDE.md](IMPLEMENTATION_GUIDE.md) - Complete documentation
- [docs/](docs/) - Original API specifications
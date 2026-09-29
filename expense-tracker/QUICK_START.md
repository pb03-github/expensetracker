# Quick Start & Testing Guide

## 🚀 Quick Start (30 seconds)

### 1. Build & Run
```bash
cd expense-tracker
./mvnw clean spring-boot:run
```

### 2. Open Swagger UI
Navigate to: **http://localhost:8080/swagger-ui.html**

### 3. Start Testing
All endpoints are documented and ready to test directly from Swagger UI using "Try it out" buttons.

---

## 📝 API Endpoint Summary

### Create User
```
POST /api/users
Content-Type: application/json

{
  "name": "John Doe"
}

Response: 201 Created
{
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "createdAt": "2026-09-30T00:30:00Z"
}
```

### List Categories
```
GET /api/users/{userId}/categories

Response: 200 OK
[
  { "categoryId": 1, "name": "Food" },
  { "categoryId": 2, "name": "Travel" }
]
```

### List Groups
```
GET /api/users/{userId}/groups

Response: 200 OK
[
  { "groupId": 1, "name": "Personal" },
  { "groupId": 2, "name": "Work" }
]
```

### Create Expense
```
POST /api/users/{userId}/expenses
Content-Type: application/json

{
  "amount": 500.00,
  "categoryId": 1,
  "groupId": 1
}

Response: 201 Created
{
  "expenseId": 1001,
  "amount": 500.00,
  "category": { "categoryId": 1, "name": "Food" },
  "group": { "groupId": 1, "name": "Personal" },
  "createdAt": "2026-09-30T00:35:00Z"
}
```

### List Expenses (with Filters)
```
GET /api/users/{userId}/expenses?categoryId=1&groupId=1&from=2026-09-01&to=2026-09-30&page=0&size=20

Response: 200 OK
{
  "content": [
    {
      "expenseId": 1001,
      "amount": 500.00,
      "category": { "categoryId": 1, "name": "Food" },
      "group": { "groupId": 1, "name": "Personal" },
      "createdAt": "2026-09-30T00:35:00Z"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

### Get Expense Summary
```
GET /api/users/{userId}/expenses/summary

Response: 200 OK
{
  "totalAmount": 10840.00,
  "categoryTotals": [
    {
      "categoryId": 1,
      "categoryName": "Food",
      "amount": 5240.00
    }
  ],
  "groupTotals": [
    {
      "groupId": 1,
      "groupName": "Personal",
      "amount": 7500.00
    }
  ]
}
```

---

## 🧪 Testing Workflow

### Step 1: Create a User
1. Open Swagger UI: http://localhost:8080/swagger-ui.html
2. Expand "Users" section
3. Click "POST /api/users"
4. Click "Try it out"
5. Enter: `{ "name": "Test User" }`
6. Click "Execute"
7. **Copy the `userId` from response** - you'll need it for all other requests

### Step 2: List Categories
1. Expand "Categories" section
2. Click "GET /api/users/{userId}/categories"
3. Click "Try it out"
4. Paste the `userId` from Step 1
5. Click "Execute"
6. Note the available categories (e.g., "Food" with categoryId: 1)

### Step 3: Create an Expense
1. Expand "Expenses" section
2. Click "POST /api/users/{userId}/expenses"
3. Click "Try it out"
4. Paste the `userId`
5. Enter request body:
   ```json
   {
     "amount": 150.50,
     "categoryId": 1,
     "groupId": null
   }
   ```
6. Click "Execute"

### Step 4: List Expenses
1. Click "GET /api/users/{userId}/expenses"
2. Click "Try it out"
3. Paste the `userId`
4. Leave filters empty (optional)
5. Click "Execute"
6. See all your created expenses

### Step 5: Get Summary
1. Click "GET /api/users/{userId}/expenses/summary"
2. Click "Try it out"
3. Paste the `userId`
4. Click "Execute"
5. See totals breakdown by category and group

---

## 🔧 Available Configuration

### Endpoints
- API Base: http://localhost:8080/api
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs (JSON): http://localhost:8080/v3/api-docs
- H2 Console: http://localhost:8080/h2-console

### Database
Default: **H2 In-Memory** (perfect for local development)
- No setup required
- Data resets on application restart
- Good for testing

### Switching to PostgreSQL
Update `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/expense_tracker
    username: postgres
    password: your_password
```

---

## 📊 Test Data Scenarios

### Scenario 1: Simple Expense Tracking
```bash
# Create user
POST /api/users → userId A

# Create expense without category/group
POST /api/users/{A}/expenses → { "amount": 25.00 }

# Get summary
GET /api/users/{A}/expenses/summary
```

### Scenario 2: Categorized Expenses
```bash
# Create user
POST /api/users → userId B

# List categories to see available options
GET /api/users/{B}/categories → returns Food, Travel, etc.

# Create expense with category
POST /api/users/{B}/expenses → { "amount": 50.00, "categoryId": 1 }

# Get summary
GET /api/users/{B}/expenses/summary
```

### Scenario 3: Filtered Listing
```bash
# Create multiple expenses over time
POST /api/users/{B}/expenses → { "amount": 50.00, "categoryId": 1 }
POST /api/users/{B}/expenses → { "amount": 150.00, "categoryId": 2 }
POST /api/users/{B}/expenses → { "amount": 75.00, "categoryId": 1 }

# Filter by category
GET /api/users/{B}/expenses?categoryId=1&page=0&size=20

# Filter by date range
GET /api/users/{B}/expenses?from=2026-09-01&to=2026-09-30&page=0&size=20

# Combine filters
GET /api/users/{B}/expenses?categoryId=1&from=2026-09-01&to=2026-09-30&page=0&size=20
```

---

## ✅ Validation Rules

### User
- Name: Required, max 100 characters, must be non-blank after trimming

### Category/Group
- Automatically loaded from database
- User-scoped (cannot access other user's categories/groups)
- Returns 404 if category/group doesn't belong to user

### Expense Amount
- Required
- Must be > 0
- Max 15 integer digits + 4 decimal places (NUMERIC(19,4))

### Pagination
- Page: 0-indexed, must be ≥ 0
- Size: 1-100 (default: 20)
- Query returns 400 if invalid

### Date Filters
- Format: YYYY-MM-DD (ISO 8601)
- Inclusive on both ends
- Must have from ≤ to
- Returns 400 if invalid

---

## 🐛 Troubleshooting

### Port 8080 Already in Use
```bash
# Change port in application.yml
server:
  port: 8081
```

### H2 Database Connection
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:expense_tracker;MODE=PostgreSQL`
- Username: `sa`
- Password: (empty)

### Migrations Not Running
- Check `db/migration/` folder exists
- Ensure SQL files follow naming: `V1__*.sql`
- Check application logs for Flyway errors

### 404 on User/Category/Group
- Verify the UUID/ID is correct
- Ensure the resource belongs to the specified user
- H2 data resets on application restart

---

## 📚 Next Steps

1. **Implement Authentication** - Add security layer (OAuth2/JWT)
2. **Add Validations** - Enhance input validation
3. **Create Categories/Groups** - Add POST endpoints for maintenance
4. **Add Tests** - Write unit and integration tests
5. **Deploy** - Switch to PostgreSQL and deploy to production

---

## 💡 Tips

- **Copy User ID**: When testing, keep the userId handy in a text editor
- **Test Different Users**: Create multiple users to test data isolation
- **Try Filters**: Experiment with pagination and filters
- **Check H2 Console**: View raw data and debug queries
- **Read Error Messages**: They provide helpful validation feedback

---

Happy Testing! 🎉

# API Testing Examples with cURL

This file contains ready-to-use cURL commands for testing all Expense Tracker endpoints.

> **Note**: Replace `$USER_ID` with an actual UUID from your user creation response.

## 📌 Prerequisites

```bash
# Ensure the application is running on localhost:8080
# If running on a different host/port, update BASE_URL
BASE_URL="http://localhost:8080/api"
```

---

## 1️⃣ Create a User

### Request
```bash
curl -X POST "$BASE_URL/users" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe"
  }'
```

### Expected Response (201 Created)
```json
{
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "createdAt": "2026-09-30T00:30:00Z"
}
```

### Save for Later Use
```bash
# After creating a user, save the userId
USER_ID="550e8400-e29b-41d4-a716-446655440000"
```

---

## 2️⃣ List Categories

### Request
```bash
curl "$BASE_URL/users/$USER_ID/categories" \
  -H "Accept: application/json"
```

### Expected Response (200 OK)
```json
[
  { "categoryId": 1, "name": "Food" },
  { "categoryId": 2, "name": "Travel" },
  { "categoryId": 3, "name": "Utilities" }
]
```

### Save Category IDs for Later Use
```bash
CATEGORY_ID=1  # Food
GROUP_ID=1     # (from groups endpoint)
```

---

## 3️⃣ List Groups

### Request
```bash
curl "$BASE_URL/users/$USER_ID/groups" \
  -H "Accept: application/json"
```

### Expected Response (200 OK)
```json
[
  { "groupId": 1, "name": "Personal" },
  { "groupId": 2, "name": "Work" }
]
```

---

## 4️⃣ Create an Expense

### 4a. Expense with Category and Group
```bash
curl -X POST "$BASE_URL/users/$USER_ID/expenses" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 500.00,
    "categoryId": 1,
    "groupId": 1
  }'
```

### 4b. Expense with Category Only
```bash
curl -X POST "$BASE_URL/users/$USER_ID/expenses" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 150.50,
    "categoryId": 2,
    "groupId": null
  }'
```

### 4c. Expense with No Category/Group
```bash
curl -X POST "$BASE_URL/users/$USER_ID/expenses" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 25.00
  }'
```

### Expected Response (201 Created)
```json
{
  "expenseId": 1001,
  "amount": 500.00,
  "category": { "categoryId": 1, "name": "Food" },
  "group": { "groupId": 1, "name": "Personal" },
  "createdAt": "2026-09-30T00:35:00Z"
}
```

---

## 5️⃣ List Expenses

### 5a. Get All Expenses (No Filters)
```bash
curl "$BASE_URL/users/$USER_ID/expenses" \
  -H "Accept: application/json"
```

### 5b. Filter by Category
```bash
curl "$BASE_URL/users/$USER_ID/expenses?categoryId=1" \
  -H "Accept: application/json"
```

### 5c. Filter by Group
```bash
curl "$BASE_URL/users/$USER_ID/expenses?groupId=1" \
  -H "Accept: application/json"
```

### 5d. Filter by Category AND Group
```bash
curl "$BASE_URL/users/$USER_ID/expenses?categoryId=1&groupId=1" \
  -H "Accept: application/json"
```

### 5e. Filter by Date Range
```bash
curl "$BASE_URL/users/$USER_ID/expenses?from=2026-09-01&to=2026-09-30" \
  -H "Accept: application/json"
```

### 5f. Filter by Date and Category
```bash
curl "$BASE_URL/users/$USER_ID/expenses?categoryId=1&from=2026-09-01&to=2026-09-30" \
  -H "Accept: application/json"
```

### 5g. Pagination (Page 1, Size 10)
```bash
curl "$BASE_URL/users/$USER_ID/expenses?page=1&size=10" \
  -H "Accept: application/json"
```

### 5h. All Filters Combined
```bash
curl "$BASE_URL/users/$USER_ID/expenses?categoryId=1&groupId=1&from=2026-09-01&to=2026-09-30&page=0&size=20" \
  -H "Accept: application/json"
```

### Expected Response (200 OK)
```json
{
  "content": [
    {
      "expenseId": 1001,
      "amount": 500.00,
      "category": { "categoryId": 1, "name": "Food" },
      "group": { "groupId": 1, "name": "Personal" },
      "createdAt": "2026-09-30T00:35:00Z"
    },
    {
      "expenseId": 1002,
      "amount": 150.50,
      "category": { "categoryId": 2, "name": "Travel" },
      "group": null,
      "createdAt": "2026-09-30T12:40:00Z"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 2,
  "totalPages": 1
}
```

---

## 6️⃣ Get Expense Summary

### Request
```bash
curl "$BASE_URL/users/$USER_ID/expenses/summary" \
  -H "Accept: application/json"
```

### Expected Response (200 OK)
```json
{
  "totalAmount": 10840.00,
  "categoryTotals": [
    {
      "categoryId": 1,
      "categoryName": "Food",
      "amount": 5240.00
    },
    {
      "categoryId": 2,
      "categoryName": "Travel",
      "amount": 3500.00
    }
  ],
  "groupTotals": [
    {
      "groupId": 1,
      "groupName": "Personal",
      "amount": 7500.00
    },
    {
      "groupId": 2,
      "groupName": "Work",
      "amount": 3340.00
    }
  ]
}
```

---

## ❌ Error Scenarios

### Invalid User ID
```bash
curl "$BASE_URL/users/invalid-uuid/expenses" \
  -H "Accept: application/json"
```

Response (400 Bad Request):
```json
{
  "timestamp": "2026-09-30T00:40:00Z",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "Invalid UUID",
  "path": "/api/users/invalid-uuid/expenses"
}
```

### Non-existent User
```bash
curl "$BASE_URL/users/550e8400-e29b-41d4-a716-999999999999/expenses" \
  -H "Accept: application/json"
```

Response (404 Not Found):
```json
{
  "timestamp": "2026-09-30T00:40:00Z",
  "status": 404,
  "error": "NOT_FOUND",
  "message": "User not found: 550e8400-e29b-41d4-a716-999999999999",
  "path": "/api/users/550e8400-e29b-41d4-a716-999999999999/expenses"
}
```

### Invalid Amount
```bash
curl -X POST "$BASE_URL/users/$USER_ID/expenses" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": -50.00,
    "categoryId": 1
  }'
```

Response (400 Bad Request):
```json
{
  "timestamp": "2026-09-30T00:40:00Z",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "Amount must be greater than zero",
  "path": "/api/users/$USER_ID/expenses"
}
```

### Invalid Category (Doesn't Exist)
```bash
curl -X POST "$BASE_URL/users/$USER_ID/expenses" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 50.00,
    "categoryId": 9999
  }'
```

Response (404 Not Found):
```json
{
  "timestamp": "2026-09-30T00:40:00Z",
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Category not found: 9999",
  "path": "/api/users/$USER_ID/expenses"
}
```

---

## 🔗 Batch Testing Script

Save this as `test-api.sh`:

```bash
#!/bin/bash

BASE_URL="http://localhost:8080/api"

echo "🧪 Testing Expense Tracker API"
echo "=============================="

# 1. Create user
echo -e "\n1️⃣  Creating user..."
USER_RESPONSE=$(curl -s -X POST "$BASE_URL/users" \
  -H "Content-Type: application/json" \
  -d '{"name": "Test User"}')
USER_ID=$(echo $USER_RESPONSE | grep -o '"userId":"[^"]*' | cut -d'"' -f4)
echo "Created user: $USER_ID"

# 2. List categories
echo -e "\n2️⃣  Listing categories..."
curl -s "$BASE_URL/users/$USER_ID/categories" | jq .

# 3. List groups
echo -e "\n3️⃣  Listing groups..."
curl -s "$BASE_URL/users/$USER_ID/groups" | jq .

# 4. Create expenses
echo -e "\n4️⃣  Creating expenses..."
curl -s -X POST "$BASE_URL/users/$USER_ID/expenses" \
  -H "Content-Type: application/json" \
  -d '{"amount": 500.00, "categoryId": 1}' | jq .

# 5. List expenses
echo -e "\n5️⃣  Listing expenses..."
curl -s "$BASE_URL/users/$USER_ID/expenses" | jq .

# 6. Get summary
echo -e "\n6️⃣  Getting summary..."
curl -s "$BASE_URL/users/$USER_ID/expenses/summary" | jq .

echo -e "\n✅ Testing complete!"
```

Run with:
```bash
chmod +x test-api.sh
./test-api.sh
```

---

## 📊 Performance Testing

### Create 100 Expenses
```bash
#!/bin/bash
BASE_URL="http://localhost:8080/api"
USER_ID="550e8400-e29b-41d4-a716-446655440000"

for i in {1..100}; do
  curl -s -X POST "$BASE_URL/users/$USER_ID/expenses" \
    -H "Content-Type: application/json" \
    -d "{\"amount\": $((RANDOM % 1000 + 1)).00, \"categoryId\": $((RANDOM % 3 + 1))}" > /dev/null
  echo "Created expense $i"
done
```

### Test Pagination
```bash
# Get first 10
curl "$BASE_URL/users/$USER_ID/expenses?page=0&size=10" | jq .

# Get next 10
curl "$BASE_URL/users/$USER_ID/expenses?page=1&size=10" | jq .

# Get last 10
curl "$BASE_URL/users/$USER_ID/expenses?page=9&size=10" | jq .
```

---

## 💡 Tips

1. **Use jq for pretty printing**: `curl ... | jq .`
2. **Save responses**: `curl ... > response.json`
3. **Test invalid inputs** to verify error handling
4. **Use date filters** to limit result sets
5. **Monitor pagination** with different page sizes
6. **Check Swagger UI** for interactive testing: http://localhost:8080/swagger-ui.html

---

## 📚 Related Resources

- [QUICK_START.md](QUICK_START.md) - Quick testing guide
- [IMPLEMENTATION_GUIDE.md](IMPLEMENTATION_GUIDE.md) - Full documentation
- [Swagger UI](http://localhost:8080/swagger-ui.html) - Interactive API explorer

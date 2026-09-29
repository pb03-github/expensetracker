# Create expense

`POST /api/users/{userId}/expenses` -> `201 Created`. Request DTO fields: `amount` required `BigDecimal`, greater than zero and within `NUMERIC(19,4)`; `categoryId` and `groupId` optional nullable `Long`. Per [D3](../decisions.md), both may be absent. Reject unrecognized/invalid values with 400.

```json
{ "amount": 500.00, "categoryId": 1, "groupId": 1 }
```

```json
{
  "expenseId": 1001,
  "amount": 500.00,
  "category": { "categoryId": 1, "name": "Food" },
  "group": { "groupId": 1, "name": "Personal" },
  "createdAt": "2026-09-30T00:35:00Z"
}
```

Within a transaction: require existing user; load each supplied category/group by ID **and user ID**; return 404 for missing or foreign-owned references; create expense for that user; assign server timestamp; persist. Omitted associations are `null` in response. Never load a reference by ID alone and attach it to a different user's expense. Reuse the same `ExpenseResponse` DTO for listing.
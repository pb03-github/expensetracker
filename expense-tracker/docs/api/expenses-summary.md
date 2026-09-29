# Expense summary

`GET /api/users/{userId}/expenses/summary` -> `200 OK`. Unknown user -> 404. All-time, user-scoped totals; no dashboard/landing table. Query database aggregates, not a full expense list. Per [D5](../decisions.md), expenses with null category/group count in `totalAmount` but not the respective breakdown. Each grouping is independent; do not add category and group totals together.

```json
{
  "totalAmount": 10840.00,
  "categoryTotals": [
    { "categoryId": 1, "categoryName": "Food", "amount": 5240.00 }
  ],
  "groupTotals": [
    { "groupId": 1, "groupName": "Personal", "amount": 7500.00 }
  ]
}
```

No expenses -> `totalAmount: 0`, `categoryTotals: []`, `groupTotals: []`. Order breakdowns by amount descending, then ID ascending. Keep the aggregate query restricted by user ID and avoid N+1 name lookups by joining category/group in the grouped query.
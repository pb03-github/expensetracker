# List expenses

`GET /api/users/{userId}/expenses` -> `200 OK`. Optional `categoryId`, `groupId`, `from`, `to`, `page` (default 0), `size` (default 20, maximum 100). Filters combine with AND, always include `userId`. Unknown user -> 404; malformed filters, negative page, size outside 1..100, or `from > to` -> 400. A valid filter with no matches returns an empty page. Date filters follow [D5](../decisions.md) (UTC date boundaries).

```text
GET /api/users/{userId}/expenses?categoryId=1&groupId=2&from=2026-09-01&to=2026-09-30&page=0&size=20
```

```json
{
  "content": [
    {
      "expenseId": 1001,
      "amount": 500.00,
      "category": { "categoryId": 1, "name": "Food" },
      "group": null,
      "createdAt": "2026-09-30T00:35:00Z"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

Default order: `createdAt DESC, expenseId DESC` for stable paging on timestamp ties. This bounded object response adopts [D4](../decisions.md), superseding the earlier unbounded-array example. Fetch associated names without N+1 queries. Filtering by a foreign-owned category/group ID must not reveal its owner: just return an empty result for this user's query.
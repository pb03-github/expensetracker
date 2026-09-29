# List categories

`GET /api/users/{userId}/categories` -> `200 OK` with only the user's categories. Unknown user -> 404; no categories -> `[]`. Sort by name, then ID for stable results.

```json
[
  { "categoryId": 1, "name": "Food" },
  { "categoryId": 2, "name": "Travel" }
]
```

Category ownership: `user_id` FK; names max 100, nonblank, unique per user. If [D2](../decisions.md) is accepted, add `POST /api/users/{userId}/categories` with `{ "name": "Food" }`, `201 Created`, trimmed nonblank name, user scoping, 404 missing user, 409 duplicate name; settle response shape before implementing it. No category write endpoint belongs to the initial six-endpoint contract yet.
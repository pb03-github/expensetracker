# List groups

`GET /api/users/{userId}/groups` -> `200 OK` with only the user's groups. Unknown user -> 404; no groups -> `[]`. Sort by name, then ID for stable results.

```json
[
  { "groupId": 1, "name": "Personal" },
  { "groupId": 2, "name": "Work" }
]
```

Group ownership: `user_id` FK; names max 100, nonblank, unique per user. If [D2](../decisions.md) is accepted, add `POST /api/users/{userId}/groups` with `{ "name": "Personal" }`, `201 Created`, trimmed nonblank name, user scoping, 404 missing user, 409 duplicate name; settle response shape before implementing it. No group write endpoint belongs to the initial six-endpoint contract yet.
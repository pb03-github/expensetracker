# Create user

`POST /api/users` -> `201 Created`. Server assigns random UUID and creation timestamp; request cannot set either. The input below requires [D1](../decisions.md) to be accepted so the name is actually stored, rather than silently discarded.

```json
{ "name": "Pankaj" }
```

```json
{
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "createdAt": "2026-09-30T00:30:00Z"
}
```

Validate nonblank trimmed name with a maximum of 100 characters if D1 is accepted. Return 400 for invalid body; keep entity and response DTO separate. The user ID is stable for all owned records. Do not expose this unauthenticated endpoint publicly.
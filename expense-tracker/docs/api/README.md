# REST API index

Base path: `/api`. JSON request/response bodies. Only read the endpoint file you are changing plus the shared rules here.

| Endpoint | Contract |
| --- | --- |
| `POST /api/users` | [Create user](users.md) |
| `GET /api/users/{userId}/categories` | [List categories](categories.md) |
| `GET /api/users/{userId}/groups` | [List groups](groups.md) |
| `POST /api/users/{userId}/expenses` | [Create expense](expenses-create.md) |
| `GET /api/users/{userId}/expenses` | [List expenses](expenses-list.md) |
| `GET /api/users/{userId}/expenses/summary` | [Expense summary](expenses-summary.md) |

Validate UUID paths, request bodies and query parameters at the boundary. Existing resources of another user are not visible: respond 404 to avoid information leaks. Use 400 for invalid input, 404 for absent user or associated resource, 409 for duplicate user-scoped names when category/group creation is added, and 201 for creation. A common `@RestControllerAdvice` returns:

```json
{
  "timestamp": "2026-09-30T00:40:00Z",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "Amount must be greater than zero",
  "path": "/api/users/.../expenses"
}
```

Use stable codes such as `VALIDATION_ERROR`, `NOT_FOUND`, `CONFLICT`; do not return stack traces or entity JSON. `createdAt` is server-generated UTC `Instant`. Authentication is not implemented; path identity is for local use only. See [security](../security.md) before deployment.
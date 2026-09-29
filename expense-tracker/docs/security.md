# Ownership and security

All lookups, list queries and aggregates are scoped to the requesting user. Check the user exists before writes and user-scoped reads. When creating an expense, resolve an optional category and group with both ID and user ID; reject missing or foreign-owned references with 404 without disclosing another user's data. Never accept a user ID in the request body as ownership authority. Keep endpoint DTOs separate from entities.

Path-based UUID identity is **local-development only**: anyone who knows a UUID can read/write that user's data. Do not expose the service publicly until real authentication and authorization derive user identity from a trusted principal and enforce it on every endpoint. Do not treat UUID obscurity as access control.

Use JPA parameter binding, Jakarta request validation, no plaintext passwords, no secrets in source or logs, environment-based DB credentials, HTTPS in deployment, restricted CORS for a future client, and private database networking where available. Avoid logging expense values or sensitive user information. Keep error responses consistent without leaking internal exceptions.
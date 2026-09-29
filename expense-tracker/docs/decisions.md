# Decisions and open questions

Keep this file short. Mark an item resolved and update its owning contract before implementation; avoid repeating the original specification.

| ID | Status | Question / proposed choice |
| --- | --- | --- |
| D1 | Open | `POST /api/users` requests `name` but users schema has no name column. Proposed: persist a trimmed nonblank `name VARCHAR(100)` and retain response shape with `userId`, `createdAt`; decide before V1 migration. |
| D2 | Open | V1 says maintain personal categories/groups, but initial contract only has GET. Proposed: include `POST` for both so fresh users can populate them; otherwise explicitly defer maintenance and document how test data is seeded. |
| D3 | Proposed | Both category and group may be null for an expense; service and DB permit it in V1. |
| D4 | Proposed | Paginated expense list uses an object with `content`, `page`, `size`, `totalElements`, `totalPages` from V1, not an unbounded array. This follows the later pagination contract rather than the early GET example. |
| D5 | Proposed | Date filters are UTC calendar dates: `from` inclusive, `to` inclusive, translated to `[from 00:00Z, dayAfterTo 00:00Z)`. Summary is all-time for the user; null associations contribute to total only, not category/group breakdowns. |
| D6 | Proposed | Persist exact per-user names after trim, with case-sensitive uniqueness. Decide whether case-insensitive duplicates should also be rejected before exposing writes. |
| D7 | Required before public release | Replace userId-as-identity in URL with authenticated principal ownership and authorization; UUID path values alone provide no security. |

When a choice is finalized, change status to `Accepted` with the date and update the relevant API/database docs.
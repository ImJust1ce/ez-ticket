# Story E1-S2 — CRUD Event + validation + OpenAPI

**Status:** review  
**Epic:** E1 — Monolith CRUD Foundation

## Story

As an **Admin**, I want to create and manage events (name, venue, start time, publish status),  
so that **Users** can browse published events via the public API.

## Acceptance Criteria

1. Flyway `V2__create_events.sql` tạo bảng `events` (snake_case) khớp JPA entity.
2. Admin API (`/api/admin/v1/events`, header `X-Admin-Key`) — CRUD đầy đủ; key sai → `401`.
3. Public API (`/api/v1/events`) — chỉ event `PUBLISHED`; chi tiết unpublished → `404`.
4. Jakarta Validation trên request DTO; lỗi → envelope `400` + `VALIDATION_ERROR`.
5. Response dùng envelope `{ data, meta }`; không trả entity JPA trực tiếp.
6. OpenAPI hiển thị endpoints Event (springdoc).
7. Tests: service/repository + MockMvc public + admin (Testcontainers).

## Tasks / Subtasks

- [x] Flyway V2 + `Event` entity + `EventStatus` enum
- [x] `EventRepository`, `EventMapper` (MapStruct), DTOs
- [x] `EventService` — CRUD + publish filter logic
- [x] `EventController` (public) + `AdminEventController` + `AdminKeyInterceptor`
- [x] Tests (service integration + MockMvc public/admin)

## Dev Notes

### Architecture (bắt buộc)

- Layered: `controller` → `service` → `repository` → `entity` [architecture.md]
- `@Transactional` trên `EventService` only
- Money/datetime: `Instant` API ISO-8601 UTC

### Event schema (V2)

| Column | Type | Notes |
|--------|------|-------|
| id | BIGSERIAL PK | |
| name | VARCHAR(255) NOT NULL | |
| venue | VARCHAR(255) NOT NULL | |
| starts_at | TIMESTAMPTZ NOT NULL | |
| status | VARCHAR(20) NOT NULL | `DRAFT`, `PUBLISHED` |
| created_at | TIMESTAMPTZ | |
| updated_at | TIMESTAMPTZ | |

### API contract

| Method | Path | Auth |
|--------|------|------|
| GET | `/api/v1/events` | Public — PUBLISHED only |
| GET | `/api/v1/events/{id}` | Public — PUBLISHED only |
| GET | `/api/admin/v1/events` | `X-Admin-Key` — all |
| GET | `/api/admin/v1/events/{id}` | Admin |
| POST | `/api/admin/v1/events` | Admin — `201` |
| PUT | `/api/admin/v1/events/{id}` | Admin |
| DELETE | `/api/admin/v1/events/{id}` | Admin — `204` |

### References

- [architecture.md](../planning-artifacts/architecture.md) — API envelope, admin key phase 1–3
- [epics-stories-10-phases.md](../planning-artifacts/epics-stories-10-phases.md) — E1-S2
- [e1-s1-base-project-setup.md](./e1-s1-base-project-setup.md) — scaffold đã có

## Dev Agent Record

### Agent Model Used

Composer

### Completion Notes List

- CRUD Event hoàn chỉnh: public chỉ `PUBLISHED`, admin `X-Admin-Key`
- Flyway `V2__create_events.sql` + JPA `Event` entity
- Chạy `mvn test` local (cần Docker cho Testcontainers) để xác nhận

### File List

- `src/main/resources/db/migration/V2__create_events.sql`
- `src/main/java/com/ezticket/entity/Event.java`
- `src/main/java/com/ezticket/entity/enums/EventStatus.java`
- `src/main/java/com/ezticket/repository/EventRepository.java`
- `src/main/java/com/ezticket/service/EventService.java`
- `src/main/java/com/ezticket/mapper/EventMapper.java`
- `src/main/java/com/ezticket/dto/request/CreateEventRequest.java`
- `src/main/java/com/ezticket/dto/request/UpdateEventRequest.java`
- `src/main/java/com/ezticket/dto/response/EventResponse.java`
- `src/main/java/com/ezticket/controller/EventController.java`
- `src/main/java/com/ezticket/controller/AdminEventController.java`
- `src/main/java/com/ezticket/config/AdminKeyInterceptor.java`
- `src/main/java/com/ezticket/config/WebMvcConfig.java`
- `src/test/java/com/ezticket/service/EventServiceIntegrationTest.java`
- `src/test/java/com/ezticket/controller/EventControllerTest.java`
- `src/test/java/com/ezticket/controller/AdminEventControllerTest.java`

## Change Log

- 2026-05-20: E1-S2 — Event CRUD, Flyway V2, admin key, tests

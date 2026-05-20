# Story E1-S1 — Base project Spring Boot + DB migration + package structure

**Status:** review  
**Epic:** E1 — Monolith CRUD Foundation

## Story

Là developer, tôi cần scaffold Spring Boot với PostgreSQL/Flyway và cấu trúc layered (`controller/service/repository/entity`) để các story E1-S2+ triển khai nhất quán.

## Acceptance Criteria

- [x] Spring Boot 3.5 + Java 21, Maven build thành công
- [x] PostgreSQL + Flyway `V1__baseline.sql` chạy được
- [x] Package structure: `controller`, `service`, `repository`, `entity`, `dto`, `mapper`, `exception`, `config`, `scheduler`
- [x] API envelope `{ data, meta }` + `GlobalExceptionHandler`
- [x] `docker-compose.yml` cho Postgres local
- [x] Tests pass (context + Flyway + health endpoint)

## Tasks / Subtasks

- [x] Cập nhật `pom.xml` (Boot 3.5.14, JPA, Flyway, PostgreSQL, springdoc, MapStruct, Testcontainers)
- [x] `TicketingSystemApplication` + `application.yml`
- [x] Flyway V1 baseline migration
- [x] Layered packages + `HealthController` smoke endpoint
- [x] Shared API/error infrastructure
- [x] `docker-compose.yml`, `.env.example`, `README.md`
- [x] Unit/integration tests

## Dev Agent Record

### Implementation Plan

- Layered monolith per `architecture.md` (AD-01, AD-09)
- Flyway baseline table `application_baseline` — domain tables deferred to E1-S2
- Testcontainers for CI/local `mvn test` without manual DB setup

### Completion Notes

- Replaced skeleton `org.example` Maven project with full Spring Boot scaffold
- All tests green via Testcontainers PostgreSQL 16

## File List

- `pom.xml`
- `docker-compose.yml`
- `.env.example`
- `README.md`
- `src/main/java/com/ezticket/TicketingSystemApplication.java`
- `src/main/java/com/ezticket/controller/HealthController.java`
- `src/main/java/com/ezticket/config/*.java`
- `src/main/java/com/ezticket/dto/**/*.java`
- `src/main/java/com/ezticket/exception/*.java`
- `src/main/java/com/ezticket/{service,repository,entity,mapper,scheduler}/package-info.java`
- `src/main/resources/application.yml`
- `src/main/resources/application-local.yml`
- `src/main/resources/db/migration/V1__baseline.sql`
- `src/test/java/com/ezticket/**/*.java`
- Deleted: `src/main/java/org/example/Main.java`

## Change Log

- 2026-05-20: E1-S1 implemented — Spring Boot scaffold, Flyway baseline, layered packages

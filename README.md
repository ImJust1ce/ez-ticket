# EZ Ticket (ticketing-system)

Monolith Spring Boot — layered `controller` / `service` / `repository` / `entity`.

## Prerequisites

- Java 21
- Maven 3.9+
- Docker (PostgreSQL local)

## Quick start

```bash
# 1. Start PostgreSQL
docker compose up -d

# 2. Run application
mvn spring-boot:run

# 3. Verify
curl -s http://localhost:8080/api/v1/health | jq
```

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

## Tests

```bash
mvn test
```

Uses Testcontainers (PostgreSQL) — Docker must be running.

## Configuration

| Variable | Default |
|----------|---------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/ticketing` |
| `SPRING_DATASOURCE_USERNAME` | `ticketing` |
| `SPRING_DATASOURCE_PASSWORD` | `ticketing` |
| `APP_ADMIN_KEY` | `dev-admin-key` |

Copy `.env.example` for local overrides.

## Project layout

```
src/main/java/com/ezticket/
├── controller/
├── service/
├── repository/
├── entity/
├── dto/
├── mapper/
├── exception/
├── config/
└── scheduler/
```

Flyway migrations: `src/main/resources/db/migration/`

# Epics & Stories Mapping - Ticketing System (10 Phases)

## Quy ước ước lượng
- `S` = 0.5-1 ngày
- `M` = 1-2 ngày
- `L` = 2-4 ngày

## Epic E1 - Monolith CRUD Foundation (Phase 1)
### Stories
- `E1-S1` Thiết lập base project Spring Boot + DB migration + cấu trúc package (M)
- `E1-S2` CRUD Event + validation + API docs cơ bản (M)
- `E1-S3` CRUD Seat và map Seat vào Event (M)
- `E1-S4` CRUD TicketType/Ticket inventory + dữ liệu seed demo (M)
- `E1-S5` Test CRUD chính (Event/Seat/Ticket) (M)

### Sprint đề xuất
- Sprint 1

## Epic E2 - Reservation & Concurrency Control (Phase 2)
### Stories
- `E2-S1` Thiết kế state machine Ticket (`AVAILABLE/HELD/SOLD`) (S)
- `E2-S2` API reserve ticket 10 phút + lưu holdExpiredAt (M)
- `E2-S3` Transaction + locking strategy chống double-book (L)
- `E2-S4` Scheduled job expire hold và release ticket (M)
- `E2-S5` Test concurrency cho reserve cùng ghế (L)

### Sprint đề xuất
- Sprint 2

## Epic E3 - Order + Fake Payment Flow (Phase 3)
### Stories
- `E3-S1` Tạo Order `PENDING` từ hold hợp lệ (M)
- `E3-S2` Fake payment endpoint `SUCCESS/FAILED` (M)
- `E3-S3` Update trạng thái success/fail/expire cho order và ticket (M)
- `E3-S4` Idempotent payment callback (M)
- `E3-S5` Integration test 3 flow: success, fail, timeout (L)

### Sprint đề xuất
- Sprint 3

## Epic E4 - Authentication & Authorization (Phase 4)
### Stories
- `E4-S1` User registration/login (M)
- `E4-S2` JWT access token + filter/exception handling (M)
- `E4-S3` Role `ADMIN/USER` và guard endpoint admin (M)
- `E4-S4` Auth test cho token invalid/expired/role mismatch (M)

### Sprint đề xuất
- Sprint 4

## Epic E5 - Async Email via RabbitMQ (Phase 5)
### Stories
- `E5-S1` Producer publish message khi order `PAID` (M)
- `E5-S2` Consumer gửi email xác nhận + QR đính kèm/link (M)
- `E5-S3` Retry policy + dead-letter queue cơ bản (M)
- `E5-S4` Monitoring/log message fail để replay (S)

### Sprint đề xuất
- Sprint 5

## Epic E6 - Domain Events via Kafka (Phase 6)
### Stories
- `E6-S1` Thiết kế schema event `OrderPaid` / `TicketSold` (M)
- `E6-S2` Publish Kafka event với key partition hợp lý (M)
- `E6-S3` Consumer nội bộ xử lý idempotent (M)
- `E6-S4` Correlation id/tracing log xuyên luồng (S)

### Sprint đề xuất
- Sprint 6

## Epic E7 - Redis Distributed Lock (Phase 7)
### Stories
- `E7-S1` Tích hợp Redis và lock abstraction cho reserve/checkout (M)
- `E7-S2` Thiết kế lease timeout + unlock an toàn (M)
- `E7-S3` Fallback behavior khi lock contention cao (S)
- `E7-S4` Test multi-instance scenario chống double-book (L)

### Sprint đề xuất
- Sprint 7

## Epic E8 - Microservices Split (Phase 8)
### Stories
- `E8-S1` Xác định bounded context và service boundaries (M)
- `E8-S2` Tách Ticket Service + DB riêng (L)
- `E8-S3` Tách Order Service + DB riêng (L)
- `E8-S4` Tách Payment/Notification Service + API contract (L)
- `E8-S5` Chạy flow end-to-end qua service-to-service (L)

### Sprint đề xuất
- Sprint 8-9

## Epic E9 - Saga Pattern (Phase 9)
### Stories
- `E9-S1` Chọn mô hình saga (orchestration/choreography) và state model (M)
- `E9-S2` Triển khai happy path saga cho checkout (L)
- `E9-S3` Triển khai compensation cho payment fail/timeout (L)
- `E9-S4` Theo dõi trạng thái saga + retry policy (M)

### Sprint đề xuất
- Sprint 10

## Epic E10 - Containerization & Test Automation (Phase 10)
### Stories
- `E10-S1` Dockerfile cho từng service và compose local stack (M)
- `E10-S2` Bộ unit/integration test chuẩn hóa theo module (L)
- `E10-S3` E2E test cho flow mua vé end-to-end (L)
- `E10-S4` CI pipeline chạy build + test + report (M)
- `E10-S5` README chạy local full stack (S)

### Sprint đề xuất
- Sprint 11

## Release Gates (khóa chất lượng)
1. Gate A (sau E3): Có thể demo mua vé hoàn chỉnh trên monolith.
2. Gate B (sau E7): Concurrency ổn định khi scale nhiều instance.
3. Gate C (sau E9): Eventual consistency có compensation thực tế.
4. Gate D (sau E10): Chạy full stack bằng Docker + test pass tự động.

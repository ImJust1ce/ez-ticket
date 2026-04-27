# Product Roadmap - Ticketing System (10 Phases)

## Mục tiêu roadmap
- Đi từ monolith cơ bản đến kiến trúc phân tán theo từng bước nhỏ, mỗi bước có giá trị học tập và có thể demo được.
- Mỗi phase phải có tiêu chí hoàn tất (Exit Criteria) trước khi sang phase tiếp theo.

## Phase 1 - Monolith CRUD Event, Seat, Ticket
### Mục tiêu
- Có hệ thống quản lý dữ liệu lõi chạy ổn định trên Spring Boot.

### Phạm vi
- CRUD cho `Event`, `Seat`, `TicketType`/`Ticket`.
- API list/search event cho user.
- Validation cơ bản và error handling thống nhất.

### Exit Criteria
- Tạo/sửa/xóa event và vé hoạt động đúng.
- Seed được dữ liệu mẫu để demo.
- Có test cho CRUD chính.

## Phase 2 - Reserve ticket + transaction + race condition
### Mục tiêu
- Giải quyết đúng bài toán giữ vé và chống double-booking.

### Phạm vi
- Reserve ticket 10 phút (`AVAILABLE -> HELD`).
- Transaction boundary rõ ràng ở service layer.
- Locking strategy (optimistic/pessimistic/unique constraint) để xử lý race condition.
- Job expire hold (`HELD -> AVAILABLE`) khi quá hạn.

### Exit Criteria
- Không thể reserve cùng 1 ghế bởi 2 request đồng thời.
- Test concurrent reserve pass.
- Timeout release chạy đúng.

## Phase 3 - Payment fake + order flow
### Mục tiêu
- Hoàn thiện flow mua vé end-to-end với payment giả lập.

### Phạm vi
- Tạo order `PENDING` từ hold.
- Payment fake API trả `SUCCESS/FAILED`.
- Cập nhật trạng thái:
- Success: `Ticket -> SOLD`, `Order -> PAID`
- Fail/Timeout: `Ticket -> AVAILABLE`, `Order -> FAILED/EXPIRED`

### Exit Criteria
- Chạy được happy path và fail path.
- Payment callback idempotent.
- Có integration test cho 3 flow: success, fail, expire.

## Phase 4 - Spring Security JWT
### Mục tiêu
- Bảo vệ API và phân quyền role-based.

### Phạm vi
- Đăng ký/đăng nhập.
- JWT access token.
- Phân quyền `ADMIN` và `USER`.
- Bảo vệ endpoint admin.

### Exit Criteria
- Endpoint admin chỉ truy cập bởi `ADMIN`.
- Token invalid/expired bị chặn đúng.
- Có test cho auth cơ bản.

## Phase 5 - RabbitMQ gửi email async
### Mục tiêu
- Tách email khỏi transaction chính để giảm độ trễ.

### Phạm vi
- Publish message khi order `PAID`.
- Consumer gửi email xác nhận + QR.
- Retry và dead-letter queue cơ bản.

### Exit Criteria
- Order thành công không bị block bởi email provider.
- Email gửi lại được khi lỗi tạm thời.
- Theo dõi được message fail.

## Phase 6 - Kafka publish event OrderPaid, TicketSold
### Mục tiêu
- Chuẩn hóa event-driven integration ở cấp domain.

### Phạm vi
- Phát sự kiện Kafka: `OrderPaid`, `TicketSold`.
- Định nghĩa event schema/version.
- Logging/tracing correlation id.

### Exit Criteria
- Event phát đúng một lần theo ngữ nghĩa đã chọn (at-least-once + idempotent consumer).
- Consumer nội bộ đọc được event và xử lý đúng.

## Phase 7 - Redis distributed lock
### Mục tiêu
- Nâng khả năng xử lý đồng thời khi scale nhiều instance.

### Phạm vi
- Redis lock cho critical section reserve/checkout.
- Thiết kế timeout/lease lock an toàn.
- Fallback khi lock fail.

### Exit Criteria
- Multi-instance reserve vẫn không double-book.
- Lock timeout không gây deadlock logic.

## Phase 8 - Tách microservices
### Mục tiêu
- Chia bounded context rõ ràng để chuẩn bị kiến trúc phân tán.

### Phạm vi
- Tách tối thiểu: `Ticket Service`, `Order Service`, `Payment Service`, `Notification Service`.
- API contract giữa service.
- Database per service (hoặc schema tách biệt giai đoạn đầu).

### Exit Criteria
- Flow đặt vé chạy xuyên service.
- Không có shared write trực tiếp giữa DB các service.

## Phase 9 - Saga pattern
### Mục tiêu
- Đảm bảo eventual consistency cho flow phân tán.

### Phạm vi
- Chọn orchestration hoặc choreography.
- Định nghĩa step + compensation:
- Reserve fail/pay fail -> release ticket.
- Timeout -> expire order/hold.
- Theo dõi trạng thái saga.

### Exit Criteria
- Có ít nhất 2 kịch bản compensation chạy đúng.
- Không còn update trạng thái “mồ côi” giữa các service.

## Phase 10 - Docker + test
### Mục tiêu
- Đóng gói và kiểm thử đầy đủ để có thể chạy demo ổn định.

### Phạm vi
- Dockerfile + docker-compose cho app + DB + RabbitMQ + Kafka + Redis.
- Test strategy:
- Unit test cho domain logic
- Integration test cho repository/service/API
- Contract hoặc E2E test cho luồng chính
- CI pipeline chạy test tự động.

### Exit Criteria
- `docker compose up` chạy được toàn hệ thống.
- Bộ test cốt lõi pass ổn định.
- Có README hướng dẫn chạy local end-to-end.

## Ưu tiên thực thi (khuyến nghị)
1. Hoàn tất chắc chắn Phase 1-3 trước khi thêm hạ tầng phân tán.
2. Chỉ vào Phase 8 khi Phase 3-7 đã có test đủ mạnh.
3. Phase 9 nên làm sau khi event contract của Phase 6 ổn định.

## Rủi ro chính và cách giảm thiểu
- Scope nở nhanh: giới hạn mỗi phase vào một demo flow cụ thể.
- Quá nhiều công nghệ cùng lúc: không mở phase mới khi phase trước chưa pass exit criteria.
- Thiếu test concurrency: bắt buộc có test song song từ Phase 2 trở đi.

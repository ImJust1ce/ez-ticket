# Mini PRD - Ticketing System (MVP)

## 1. Mục tiêu
- Xây dựng một dự án nhỏ để ôn và thực hành Java, Spring Boot, JPA, transaction, scheduling, integration email/payment.
- Triển khai luồng mua vé end-to-end với trạng thái rõ ràng, tránh double-booking, có QR ticket và email xác nhận.

## 2. Phạm vi MVP
- User xem danh sách sự kiện.
- User chọn ghế hoặc loại vé.
- Hệ thống giữ vé tạm thời trong 10 phút.
- Tạo đơn hàng ở trạng thái `PENDING`.
- Thanh toán đơn hàng.
- Nếu thanh toán thành công: vé chuyển `SOLD`, đơn hàng chuyển `PAID`, gửi email chứa QR ticket.
- Nếu thanh toán thất bại hoặc quá hạn giữ vé: vé quay về `AVAILABLE`, đơn hàng thành `FAILED` hoặc `EXPIRED`.
- Admin tạo sự kiện, cấu hình vé/ghế, giá vé và số lượng.

## 3. Ngoài phạm vi (giai đoạn sau)
- Refund tự động.
- Hỗ trợ nhiều cổng thanh toán.
- Dynamic pricing.
- Hệ thống check-in tại cổng sự kiện.

## 4. Vai trò người dùng
- User: duyệt sự kiện và mua vé.
- Admin: quản lý event và inventory vé.

## 5. Business flow chính
1. User chọn event và ghế/loại vé.
2. Ticket Service thực hiện hold vé 10 phút (`HELD`).
3. Order Service tạo order `PENDING`.
4. Payment Service xử lý thanh toán.
5. Nếu thành công:
- Ticket `SOLD`
- Order `PAID`
- Tạo QR ticket
- Gửi email xác nhận
6. Nếu fail hoặc timeout:
- Ticket `AVAILABLE`
- Order `FAILED` hoặc `EXPIRED`

## 6. Mô hình trạng thái
### Ticket
- `AVAILABLE`
- `HELD`
- `SOLD`

### Order
- `PENDING`
- `PAID`
- `FAILED`
- `EXPIRED`

### Payment
- `INITIATED`
- `SUCCESS`
- `FAILED`

## 7. Quy tắc nghiệp vụ cốt lõi
- Hold timeout là 10 phút kể từ lúc tạo hold thành công.
- Một vé/ghế chỉ được hold bởi một order hợp lệ tại cùng thời điểm.
- Hết timeout mà chưa `PAID` thì release tự động.
- Payment callback phải idempotent, không update trạng thái nhiều lần.
- Chỉ đơn hàng `PAID` mới sinh QR hợp lệ.

## 8. Yêu cầu chức năng
- FR-01: User xem danh sách event và chi tiết event.
- FR-02: User chọn ghế hoặc số lượng theo loại vé.
- FR-03: Tạo hold và trả về thời điểm hết hạn hold.
- FR-04: Tạo order `PENDING` gắn với hold.
- FR-05: Tạo payment request cho order.
- FR-06: Nhận kết quả payment và cập nhật trạng thái ticket/order.
- FR-07: Tạo QR ticket sau khi thanh toán thành công.
- FR-08: Gửi email xác nhận có thông tin event, order, QR.
- FR-09: Admin tạo và quản lý event.

## 9. Yêu cầu phi chức năng
- Tính nhất quán trạng thái: ưu tiên correctness hơn performance.
- Xử lý race condition khi nhiều user chọn cùng một ghế.
- Audit cơ bản: lưu lịch sử trạng thái order và payment.
- Khả năng test: có integration test cho các flow thành công/thất bại/hết hạn.

## 10. Định hướng kỹ thuật đề xuất (gọn cho project học tập)
- Kiến trúc modular monolith với package theo domain: `event`, `ticket`, `order`, `payment`, `notification`.
- Scheduler chạy mỗi phút để expire hold quá hạn.
- Sử dụng transaction và optimistic locking (hoặc unique constraint phù hợp) để chống double-book.
- Giai đoạn đầu có thể mock cổng payment, sau đó thay bằng provider thật.

## 11. Tiêu chí thành công MVP
- User hoàn tất mua vé thành công và nhận email QR.
- Không phát sinh trường hợp 2 user cùng mua thành công một ghế.
- Vé hold quá hạn được trả lại inventory đúng hạn.
- Flow fail payment xử lý rollback trạng thái chính xác.

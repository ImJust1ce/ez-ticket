# Sprint 1 - User Stories (MVP Foundation)

## Mục tiêu sprint
- Hoàn thành luồng mua vé end-to-end với trạng thái chuẩn và xử lý timeout hold.

## Story S1-01 - Admin tạo sự kiện và vé
### User story
Là Admin, tôi muốn tạo event với thông tin cơ bản và cấu hình vé để user có thể đặt vé.

### Acceptance criteria
- Admin tạo được event với tên, địa điểm, thời gian.
- Admin cấu hình ít nhất một loại vé (giá, số lượng).
- Event ở trạng thái publish thì hiển thị cho user.

## Story S1-02 - User xem event và chọn vé
### User story
Là User, tôi muốn xem danh sách event và chọn vé để bắt đầu mua.

### Acceptance criteria
- User xem được danh sách event đang mở bán.
- User xem được chi tiết event và số lượng vé còn lại.
- User chọn được ghế hoặc số lượng theo loại vé.

## Story S1-03 - Hold vé 10 phút
### User story
Là User, khi chọn vé tôi muốn hệ thống giữ tạm 10 phút để hoàn tất thanh toán.

### Acceptance criteria
- Khi chọn vé hợp lệ, ticket chuyển sang `HELD`.
- Response trả về `holdExpiredAt`.
- Không cho hold nếu vé không còn `AVAILABLE`.
- Không cho 2 hold đồng thời trên cùng vé/ghế.

## Story S1-04 - Tạo order PENDING
### User story
Là hệ thống, tôi muốn tạo order `PENDING` ngay sau khi hold vé để theo dõi giao dịch.

### Acceptance criteria
- Mỗi hold tạo đúng một order `PENDING`.
- Order chứa thông tin user, event, ticket và tổng tiền.
- Order reference là duy nhất.

## Story S1-05 - Thanh toán và cập nhật trạng thái
### User story
Là User, tôi muốn thanh toán order để xác nhận mua vé.

### Acceptance criteria
- Tạo payment request từ order `PENDING`.
- Payment thành công: ticket `SOLD`, order `PAID`.
- Payment thất bại: order `FAILED`, ticket trả về `AVAILABLE`.
- Callback payment idempotent.

## Story S1-06 - Expire hold tự động
### User story
Là hệ thống, tôi muốn tự động hết hạn giữ vé để trả lại inventory khi user không thanh toán.

### Acceptance criteria
- Job định kỳ tìm hold quá hạn.
- Hold quá hạn: ticket `AVAILABLE`, order `EXPIRED`.
- Không expire order đã `PAID`.

## Story S1-07 - Gửi email với QR ticket
### User story
Là User, tôi muốn nhận email xác nhận có QR sau khi thanh toán thành công.

### Acceptance criteria
- Sau khi order `PAID`, hệ thống sinh QR ticket.
- Gửi email chứa mã đơn hàng, thông tin event, QR.
- Nếu gửi email lỗi thì lưu retry hoặc log lỗi có thể xử lý lại.

## Story S1-08 - Kiểm thử flow chính
### User story
Là team dev, chúng tôi muốn có test cho các flow chính để đảm bảo không regression.

### Acceptance criteria
- Có test cho happy path thanh toán thành công.
- Có test cho payment fail.
- Có test cho hold timeout.
- Có test chống double-booking cơ bản.

## Definition of Done cho Sprint 1
- Tất cả story đạt acceptance criteria.
- API chính có tài liệu request/response cơ bản.
- Chạy test tự động pass trên local.

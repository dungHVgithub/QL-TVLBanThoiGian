import React, { useContext, useEffect, useState, useRef } from "react";
import { Container, ListGroup, Spinner, Badge } from "react-bootstrap";
import Api, { authApis, endpoints } from "../configs/Api";
import { MyUserContext, NotificationContext } from "../configs/MyContexts";
import { formatDistanceToNow } from "date-fns";
import vi from "date-fns/locale/vi";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

const NotificationList = () => {
  const user = useContext(MyUserContext);
  const { setUnreadCount } = useContext(NotificationContext);
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const prevUnreadRef = useRef(0); // lưu lại số thông báo chưa đọc trước đó

  const renderTimeSafely = (input) => {
    try {
      const date = new Date(Number(input));
      if (isNaN(date.getTime())) return "Không rõ thời gian";

      return formatDistanceToNow(date, {
        addSuffix: true,
        locale: vi,
      });
    } catch (e) {
      console.error("Lỗi khi xử lý thời gian:", e);
      return "Không rõ thời gian";
    }
  };

  const markAsRead = async (notificationId, employeeId, jobId) => {
    try {
      await authApis().put(
        `${endpoints["markNotificationRead"]}?notificationId=${notificationId}&employeeId=${employeeId}&isRead=true`
      );

      toast.success("📬 Bạn đã đọc tin này");

      setNotifications((prev) =>
        prev.map((n) =>
          n.notificationId?.id === notificationId ? { ...n, isRead: true } : n
        )
      );

      setUnreadCount((prev) => Math.max(prev - 1, 0));

      if (jobId) navigate(`/job_detail/${jobId}`);
    } catch (err) {
      console.error("❌ Lỗi cập nhật trạng thái đã đọc:", err);
      toast.error("Không thể đánh dấu đã đọc");
    }
  };

  const loadNotifications = async () => {
    if (!user || !user.id || user.role !== "ROLE_EMPLOYEE") return;

    try {
      const empRes = await Api.get(`${endpoints["employeeFromUser"]}/${user.id}`);
      const employeeId = empRes.data;

      const res = await authApis().get(`${endpoints["notificationsByEmployee"]}/${employeeId}`);
      const newNotifications = res.data.map(n => ({ ...n, employeeId }));
      const newUnreadCount = newNotifications.filter(n => !n.isRead).length;

      // ✅ Hiện toast nếu có thông báo mới
      if (newUnreadCount > prevUnreadRef.current) {
        toast.info("🔔 Bạn có thông báo mới!");
      }

      prevUnreadRef.current = newUnreadCount;
      setUnreadCount(newUnreadCount);
      setNotifications(newNotifications);
    } catch (err) {
      console.error("❌ Lỗi khi load notifications:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadNotifications(); // load lần đầu

    const interval = setInterval(() => {
      loadNotifications(); // load mỗi 4 giây
    }, 4000);

    return () => clearInterval(interval); // dọn khi unmount
  }, [user?.id]);

  return (
    <Container className="mt-4">
      <h4 className="mb-4">🔔 Danh sách thông báo</h4>

      {loading ? (
        <Spinner animation="border" />
      ) : notifications.length === 0 ? (
        <p className="text-muted">Không có thông báo nào.</p>
      ) : (
        <ListGroup>
          {notifications.map((n) => {
            const content = n.notificationId?.content || "Không có nội dung";
            const createdAt = n.notificationId?.createdAt;
            const companyName = n.notificationId?.employerId?.company?.name;
            const jobId = n.notificationId?.jobId;

            return (
              <ListGroup.Item
                key={n.id}
                onClick={() =>
                  markAsRead(n.notificationId?.id, n.employeeId, jobId)
                }
                className="d-flex justify-content-between align-items-start"
                style={{
                  backgroundColor: n.isRead ? "#f8f9fa" : "#fffbea",
                  cursor: "pointer",
                }}
              >
                <div>
                  <div className="fw-bold">
                    📰 {content}
                    {companyName && (
                      <span className="text-primary"> - {companyName}</span>
                    )}
                  </div>
                  <div className="text-muted" style={{ fontSize: "0.85rem" }}>
                    ⏰ {renderTimeSafely(createdAt)}
                  </div>
                </div>
                {!n.isRead && (
                  <Badge bg="warning" text="dark">
                    Chưa đọc
                  </Badge>
                )}
              </ListGroup.Item>
            );
          })}
        </ListGroup>
      )}
    </Container>
  );
};

export default NotificationList;

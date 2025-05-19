import React, { useContext, useEffect, useState } from "react";
import { MyUserContext } from "../configs/MyContexts";
import { authApis, endpoints } from "../configs/Api";
import { Container, Card, Row, Col, Form, Button, InputGroup } from "react-bootstrap";
import { FaPhone, FaCalendarAlt, FaMapMarkerAlt } from "react-icons/fa";

const Profile = () => {
  const user = useContext(MyUserContext);
  const [profile, setProfile] = useState(null);
  const [showEditForm, setShowEditForm] = useState(false);

  useEffect(() => {
    const loadProfile = async () => {
      if (user?.id) {
        try {
          const res = await authApis().get(`${endpoints["users"]}/${user.id}`);
          const data = res.data;
          // Định dạng ngày sinh cho input[type=date]
          if (data.birthday)
            data.birthday = new Date(data.birthday).toISOString().split("T")[0];

          setProfile(data);
        } catch (err) {
          console.error("Không thể tải profile:", err);
        }
      }
    };
    loadProfile();
  }, [user]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const updated = {
        ...profile,
        birthday: profile.birthday ? new Date(profile.birthday).getTime() : null,
      };

      const res = await authApis().post("/user/update", updated);
      setProfile({
        ...res.data,
        birthday: res.data.birthday
          ? new Date(res.data.birthday).toISOString().split("T")[0]
          : ""
      });
      alert("Cập nhật thành công!");
      setShowEditForm(false);
    } catch (err) {
      console.error("Lỗi cập nhật:", err);
      alert("Có lỗi xảy ra khi lưu thông tin.");
    }
  };

  if (!profile) return <p className="text-center mt-5">Đang tải thông tin...</p>;

  return (
    <Container className="mt-5">
      <Card className="shadow p-4 mb-4">
        <h3 className="mb-4">Thông Tin Cá Nhân</h3>
        <Row>
          <Col md={6}>
            <p><strong>👤 Tên:</strong> {profile.name}</p>
            <p><strong>📧 Email:</strong> {profile.email}</p>
          </Col>
          <Col md={6}>
            <p><strong>📍 Địa chỉ:</strong> {profile.address || "Chưa cập nhật"}</p>
            <p><strong>📞 Số điện thoại:</strong> {profile.sdt || "Chưa cập nhật"}</p>
            <p><strong>🎂 Ngày sinh:</strong> {
              profile.birthday
                ? new Date(profile.birthday).toLocaleDateString("vi-VN")
                : "Chưa cập nhật"
            }</p>
          </Col>
        </Row>
        {!showEditForm && (
          <div className="text-end mt-3">
            <Button variant="primary" onClick={() => setShowEditForm(true)}>
              ✏️ Chỉnh sửa
            </Button>
          </div>
        )}
      </Card>

      {showEditForm && (
        <Card className="shadow p-4">
          <h4 className="mb-3">Cập Nhật Thông Tin</h4>
          <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3">
              <Form.Label>Số điện thoại</Form.Label>
              <InputGroup>
                <InputGroup.Text><FaPhone /></InputGroup.Text>
                <Form.Control
                  type="text"
                  value={profile.sdt || ""}
                  onChange={(e) =>
                    setProfile({ ...profile, sdt: e.target.value })
                  }
                />
              </InputGroup>
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Địa chỉ</Form.Label>
              <InputGroup>
                <InputGroup.Text><FaMapMarkerAlt /></InputGroup.Text>
                <Form.Control
                  type="text"
                  value={profile.address || ""}
                  onChange={(e) =>
                    setProfile({ ...profile, address: e.target.value })
                  }
                />
              </InputGroup>
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Ngày sinh</Form.Label>
              <InputGroup>
                <InputGroup.Text><FaCalendarAlt /></InputGroup.Text>
                <Form.Control
                  type="date"
                  value={profile.birthday || ""}
                  onChange={(e) =>
                    setProfile({ ...profile, birthday: e.target.value })
                  }
                />
              </InputGroup>
            </Form.Group>

            <div className="d-flex justify-content-between">
              <Button variant="secondary" onClick={() => setShowEditForm(false)}>
                Hủy
              </Button>
              <Button variant="primary" type="submit">
                Lưu thay đổi
              </Button>
            </div>
          </Form>
        </Card>
      )}
    </Container>
  );
};

export default Profile;

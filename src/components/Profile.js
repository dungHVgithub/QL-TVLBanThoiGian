import React, { useContext, useEffect, useState } from "react";
import { MyUserContext } from "../configs/MyContexts";
import { authApis, endpoints } from "../configs/Api";
import { Container, Card, Row, Col, Form, Button, InputGroup } from "react-bootstrap";
import { FaPhone, FaCalendarAlt, FaMapMarkerAlt } from "react-icons/fa";
import { useNavigate } from "react-router-dom";

const Profile = () => {
  const user = useContext(MyUserContext);
  const [profile, setProfile] = useState(null);
  const [showEditForm, setShowEditForm] = useState(false);
  const [documents, setDocuments] = useState([]);
  const [documentsBeingEdited, setDocumentsBeingEdited] = useState({});
  const [companyId, setCompanyId] = useState(null);
  const navigate = useNavigate();

  // Hàm ánh xạ vai trò sang tên hiển thị thân thiện
  const getRoleDisplayName = (role) => {
    switch (role) {
      case "ROLE_EMPLOYER":
        return "Nhà tuyển dụng";
      case "ROLE_EMPLOYEE":
        return "Ứng viên";
      default:
        return "Chưa xác định";
    }
  };

  useEffect(() => {
    const loadProfile = async () => {
      if (user?.id) {
        try {
          const res = await authApis().get(`${endpoints["users"]}/${user.id}`);
          const data = res.data;
          if (data.birthday)
            data.birthday = new Date(data.birthday).toISOString().split("T")[0];

          setProfile(data);

          // Nếu là nhà tuyển dụng, lấy companyId từ endpoint /employers
          if (data.role === "ROLE_EMPLOYER") {
            try {
              const employerRes = await authApis().get(`${endpoints["employers"]}`);
              // Tìm employer có userId.id khớp với user.id
              const matchedEmployer = employerRes.data.find(
                (employer) => employer.userId.id === user.id
              );
              if (matchedEmployer) {
                setCompanyId(matchedEmployer.company.id); // Lấy company.id từ employer khớp
              } else {
                console.error("Không tìm thấy employer khớp với user.id:", user.id);
              }
            } catch (err) {
              console.error("Không thể tải thông tin công ty:", err);
            }
          }
        } catch (err) {
          console.error("Không thể tải profile:", err);
        }
      }
    };

    const loadDocuments = async () => {
      if (user?.id) {
        try {
          const res = await authApis().get(`${endpoints["documentsByUser"]}/${user.id}`);
          setDocuments(res.data);
        } catch (err) {
          console.error("Không thể tải tài liệu:", err);
        }
      }
    };

    loadProfile();
    loadDocuments();
  }, [user]);

  const handleSubmit = async (e) => {
  e.preventDefault();

  try {
    if (!profile.id) {
      alert("Không tìm thấy ID người dùng!");
      return;
    }

    const updated = {
      ...profile,
      birthday: profile.birthday ? new Date(profile.birthday).getTime() : null,
    };

    console.log("Cập nhật user:", updated);

    const res = await authApis().post(endpoints.updated, updated);

    setProfile({
      ...res.data,
      birthday: res.data.birthday
        ? new Date(res.data.birthday).toISOString().split("T")[0]
        : "",
    });
    alert("Cập nhật thành công!");
    setShowEditForm(false);
  } catch (err) {
    console.error("Lỗi cập nhật:", err);
    alert("Có lỗi xảy ra khi lưu thông tin.");
  }
};

  const handleDocSubmit = async (e, docId) => {
    e.preventDefault();
    const doc = documentsBeingEdited[docId];

    try {
      const res = await authApis().put(
        `${endpoints.updateDocument}/${docId}`,
        {
          name: doc.name,
          type: doc.document_type
        }
      );

      const updatedDocs = documents.map(d => (d.id === docId ? res.data : d));
      setDocuments(updatedDocs);

      const newEditing = { ...documentsBeingEdited };
      delete newEditing[docId];
      setDocumentsBeingEdited(newEditing);

      alert("Cập nhật tài liệu thành công!");
    } catch (err) {
      console.error("Lỗi khi cập nhật tài liệu:", err);
      alert("Có lỗi xảy ra khi cập nhật tài liệu.");
    }
  };

  if (!profile) return <p className="text-center mt-5">Đang tải thông tin...</p>;

  return (
    <Container className="mt-5">
      <Card className="shadow p-4 mb-4">
        <h3 className="mb-4">Thông Tin Cá Nhân</h3>
        <Row>
          <Col md={3} className="text-center">
            <img
              src={profile.avatar || "/default-avatar.png"}
              alt="Avatar"
              className="img-thumbnail rounded-circle mb-3"
              style={{ width: "150px", height: "150px", objectFit: "cover" }}
            />
          </Col>
          <Col md={9}>
            <Row>
              <Col md={6}>
                <p><strong>👤 Tên:</strong> {profile.name}</p>
              </Col>
              <Col md={6}>
                <p><strong>📧 Email:</strong> {profile.email}</p>
              </Col>
            </Row>
            <Row>
              <Col md={6}>
                <p><strong>📍 Địa chỉ:</strong> {profile.address || "Chưa cập nhật"}</p>
              </Col>
              <Col md={6}>
                <p><strong>📞 Số điện thoại:</strong> {profile.sdt || "Chưa cập nhật"}</p>
              </Col>
            </Row>
            <Row>
              <Col md={6}>
                <p><strong>🎂 Ngày sinh:</strong> {
                  profile.birthday
                    ? new Date(profile.birthday).toLocaleDateString("vi-VN")
                    : "Chưa cập nhật"
                }</p>
              </Col>
              <Col md={6}>
                <p><strong>👑 Vai trò:</strong> {getRoleDisplayName(profile.role)}</p>
              </Col>
            </Row>
          </Col>
        </Row>
        {!showEditForm && (
          <div className="text-end mt-3">
            <Button variant="primary" onClick={() => setShowEditForm(true)} className="me-2">
              ✏️ Chỉnh sửa
            </Button>
            {profile.role === "ROLE_EMPLOYER" && companyId && (
              <>
                <Button
                  variant="success"
                  onClick={() => navigate(`/company_info/${companyId}`)}
                >
                  ℹ️ Thông tin công ty
                </Button>
              </>
            )}
          </div>
        )}
      </Card>

      {showEditForm && (
        <Card className="shadow p-4">
          <h4 className="mb-3">Cập Nhật Thông Tin</h4>
          <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3">
              <Form.Label>Họ tên</Form.Label>
              <Form.Control
                type="text"
                placeholder="Nhập họ tên"
                value={profile.name || ""}
                onChange={(e) => setProfile({ ...profile, name: e.target.value })}
              />
            </Form.Group>

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

      <Card className="shadow p-4 mt-4">
        <h4 className="mb-3">Tài Liệu Cá Nhân</h4>
        {documents.length === 0 ? (
          <p>Chưa có tài liệu nào.</p>
        ) : (
          documents.map((doc) => {
            const isEditing = !!documentsBeingEdited[doc.id];
            const editedDoc = documentsBeingEdited[doc.id] || {};

            return (
              <Form key={doc.id} onSubmit={(e) => handleDocSubmit(e, doc.id)}>
                <Row className="align-items-center mb-4">
                  <Col md={3} className="text-center">
                    <img
                      src={doc.documentPath}
                      alt={`Tài liệu: ${doc.name}`}
                      className="img-fluid rounded border"
                      style={{ width: "150px", height: "150px", objectFit: "cover" }}
                    />
                  </Col>

                  <Col md={6}>
                    {isEditing ? (
                      <>
                        <Form.Group className="mb-2">
                          <Form.Label>Tên tài liệu</Form.Label>
                          <Form.Control
                            type="text"
                            value={editedDoc.name}
                            onChange={(e) =>
                              setDocumentsBeingEdited({
                                ...documentsBeingEdited,
                                [doc.id]: {
                                  ...editedDoc,
                                  name: e.target.value
                                }
                              })
                            }
                          />
                        </Form.Group>

                        <Form.Group className="mb-2">
                          <Form.Label>Loại tài liệu</Form.Label>
                          <Form.Select
                            value={editedDoc.document_type}
                            onChange={(e) =>
                              setDocumentsBeingEdited({
                                ...documentsBeingEdited,
                                [doc.id]: {
                                  ...editedDoc,
                                  type: e.target.value
                                }
                              })
                            }
                          >
                            <option value="CV">CV</option>
                            <option value="ID">ID</option>
                            <option value="Certificate">Certificate</option>
                          </Form.Select>
                        </Form.Group>

                        <Button variant="success" type="submit" className="me-2">
                          💾 Lưu
                        </Button>
                        <Button
                          variant="secondary"
                          onClick={() => {
                            const newEditing = { ...documentsBeingEdited };
                            delete newEditing[doc.id];
                            setDocumentsBeingEdited(newEditing);
                          }}
                        >
                          ❌ Hủy
                        </Button>
                      </>
                    ) : (
                      <>
                        <p><strong>📃 Tên tài liệu:</strong> {doc.name}</p>
                        <p><strong>📄 Loại tài liệu:</strong> {doc.document_type}</p>
                        <p><strong>📅 Ngày tạo:</strong> {new Date(doc.createdDate).toLocaleDateString("vi-VN")}</p>
                        <p><strong>🕓 Ngày cập nhật:</strong> {
                          doc.updatedDate
                            ? new Date(doc.updatedDate).toLocaleString("vi-VN")
                            : "Chưa cập nhật"
                        }</p>
                        <Button
                          variant="primary"
                          onClick={() =>
                            setDocumentsBeingEdited({
                              ...documentsBeingEdited,
                              [doc.id]: {
                                name: doc.name,
                                type: doc.document_type
                              }
                            })
                          }
                        >
                          ✏️ Chỉnh sửa
                        </Button>
                      </>
                    )}
                  </Col>
                </Row>
              </Form>
            );
          })
        )}
      </Card>
    </Container>
  );
};

export default Profile;
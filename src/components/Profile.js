import React, { useContext, useEffect, useState } from "react";
import { MyUserContext } from "../configs/MyContexts";
import { authApis, endpoints } from "../configs/Api";
import { Container, Card, Row, Col, Form, Button, InputGroup, Modal, ListGroup } from "react-bootstrap";
import { FaPhone, FaCalendarAlt, FaMapMarkerAlt } from "react-icons/fa";
import { useNavigate } from "react-router-dom";

const Profile = () => {
  const user = useContext(MyUserContext);
  const [profile, setProfile] = useState(null);
  const [showEditForm, setShowEditForm] = useState(false);
  const [documents, setDocuments] = useState([]);
  const [documentsBeingEdited, setDocumentsBeingEdited] = useState({});
  const [companyId, setCompanyId] = useState(null);
  const [showApplicationsModal, setShowApplicationsModal] = useState(false);
  const [applications, setApplications] = useState([]);
  const navigate = useNavigate();

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

          if (data.role === "ROLE_EMPLOYER") {
            try {
              const employerRes = await authApis().get(`${endpoints["employers"]}`);
              const matchedEmployer = employerRes.data.find(
                (employer) => employer.userId.id === user.id
              );
              if (matchedEmployer) {
                setCompanyId(matchedEmployer.company.id);
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

  const loadApplications = async (employeeId) => {
    try {
      const res = await authApis().get(`${endpoints["employeeJob/employee"]}${employeeId}`);
      const filteredApplications = res.data.filter(
        (app) => app.jobState !== 0 && app.jobState !== 1 && app.jobId?.name
      );
      setApplications(filteredApplications);
      setShowApplicationsModal(true);
    } catch (err) {
      console.error("Lỗi khi tải danh sách ứng tuyển:", err);
      alert("Không thể tải danh sách ứng tuyển.");
    }
  };

  const handleViewApplications = async () => {
    if (profile.role === "ROLE_EMPLOYER") {
      try {
        const employeeRes = await authApis().get(`${endpoints["employees"]}`);
        const allEmployees = employeeRes.data;

        const promises = allEmployees.map(employee =>
          authApis().get(`${endpoints["employeeJob/employee"]}${employee.id}`)
            .then(res => res.data)
            .catch(err => {
              console.error(`Lỗi khi tải employeeJob cho employee ${employee.id}:`, err);
              return [];
            })
        );

        const allApplications = await Promise.all(promises);
        const filteredApplications = allApplications
          .flat()
          .filter(app => app.jobState !== 0 && app.jobState !== 1 && app.jobId?.name);

        setApplications(filteredApplications);
        setShowApplicationsModal(true);
      } catch (err) {
        console.error("Lỗi khi tải danh sách ứng tuyển:", err);
        alert("Không thể tải danh sách ứng tuyển.");
      }
    }
  };

  const updateJobState = async (employeeId, employeeJobId, jobState) => {
  if (!employeeId) {
    alert("Không thể xác định ID ứng viên. Vui lòng thử lại!");
    return;
  }

  try {
    const url = `${endpoints["employeeJob/employee"]}${employeeId}/${employeeJobId}`;
    const res = await authApis().put(url, { jobState: jobState });
    
    // Cập nhật danh sách applications: loại bỏ bản ghi có jobState là 0 hoặc 1
    const updatedApplications = applications
      .map(app => (app.id === employeeJobId ? res.data : app)) // Cập nhật bản ghi
      .filter(app => app.jobState !== 0 && app.jobState !== 1); // Lọc lại để loại bỏ bản ghi đã duyệt/từ chối
    
    setApplications(updatedApplications);
    alert(`Cập nhật trạng thái thành công!`);
    
    // Nếu không còn applications nào, có thể đóng Modal
    if (updatedApplications.length === 0) {
      setShowApplicationsModal(false);
    }
  } catch (err) {
    console.error("Lỗi khi cập nhật trạng thái:", err);
    alert("Có lỗi xảy ra khi cập nhật trạng thái. Vui lòng kiểm tra log.");
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
            {profile.role === "ROLE_EMPLOYER" && (
              <>
                {companyId && (
                  <Button
                    variant="success"
                    onClick={() => navigate(`/company_info/${companyId}`)}
                    className="me-2"
                  >
                    ℹ️ Thông tin công ty
                  </Button>
                )}
                <Button variant="info" onClick={handleViewApplications}>
                  📋 Xem danh sách ứng tuyển
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
                            <option value="Diploma">Diploma</option>
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

      <Modal
        show={showApplicationsModal}
        onHide={() => setShowApplicationsModal(false)}
        size="lg"
        centered
      >
        <Modal.Header closeButton>
          <Modal.Title>Danh Sách Ứng Tuyển</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {applications.length === 0 ? (
            <p className="text-center">Không có ứng tuyển nào đang chờ xử lý.</p>
          ) : (
            applications.map((app) => (
              <Card key={app.id} className="mb-3">
                <Card.Body>
                  <Card.Title>{app.jobId?.name}</Card.Title>
                  <ListGroup variant="flush">
                    <ListGroup.Item>
                      <strong>Ứng viên:</strong>{" "}
                      {app.employeeId?.userId?.name || "Chưa xác định"}
                    </ListGroup.Item>
                    <ListGroup.Item>
                      <strong>Lương:</strong>{" "}
                      {app.jobId?.salary ? `${app.jobId.salary} $` : "Thỏa thuận"}
                    </ListGroup.Item>
                    <ListGroup.Item>
                      <strong>Thời gian làm việc:</strong>{" "}
                      {app.jobId?.timeStart && app.jobId?.timeEnd
                        ? `${app.jobId.timeStart} - ${app.jobId.timeEnd}`
                        : "Chưa xác định"}
                    </ListGroup.Item>
                    <ListGroup.Item>
                      <strong>Địa chỉ:</strong>{" "}
                      {app.jobId?.employerId?.company?.address || "Chưa xác định"}
                    </ListGroup.Item>
                    <ListGroup.Item>
                      <strong>Mã số thuế:</strong>{" "}
                      {app.jobId?.employerId?.company?.taxCode || "Chưa xác định"}
                    </ListGroup.Item>
                  </ListGroup>
                  <div className="d-flex justify-content-end mt-2">
                    <Button
                      variant="success"
                      className="me-2"
                      onClick={() => updateJobState(app.employeeId?.id, app.id, 1)}
                    >
                      ✅ Duyệt
                    </Button>
                    <Button
                      variant="danger"
                      onClick={() => updateJobState(app.employeeId?.id, app.id, 0)}
                    >
                      ❌ Từ chối
                    </Button>
                  </div>
                </Card.Body>
              </Card>
            ))
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowApplicationsModal(false)}>
            Đóng
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
};

export default Profile;
import React, { useContext, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Container, Card, Row, Col, Button, Form } from "react-bootstrap";
import Api, { authApis, endpoints } from "../configs/Api";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "../static/companyInfo.css";
import { MyUserContext } from "../configs/MyContexts";
import FollowButton from "./FollowButton";

const CompanyInfo = () => {
  const { companyId } = useParams();
  const [companyData, setCompanyData] = useState(null);
  const [employeeId, setEmployeeId] = useState(null);
  const [followerCount, setFollowerCount] = useState(0);
  const [showEditForm, setShowEditForm] = useState(false);
  const [formData, setFormData] = useState({ name: "", address: "", taxCode: "" });

  const user = useContext(MyUserContext);
  const isLogin = user && user.role === "ROLE_EMPLOYEE";

  const loadCompanyImages = async () => {
    try {
      const res = await Api.get(`${endpoints["company_images"]}/${companyId}`);
      setCompanyData(res.data);

      if (res.data.length > 0) {
        const company = res.data[0].companyId || {};
        setFormData({
          name: company.name || "",
          address: company.address || "",
          taxCode: company.taxCode || "",
        });
      }
    } catch (err) {
      console.error("Không thể tải thông tin công ty:", err);
      toast.error("Không thể tải thông tin công ty!");
    }
  };

  const loadFollowerCount = async (employerId) => {
    try {
      const res = await Api.get(`${endpoints["followCount"]}/${employerId}`);
      setFollowerCount(res.data.count);
    } catch (err) {
      console.error("Lỗi khi lấy số lượng follower:", err);
    }
  };

  const loadEmployeeId = async (userId) => {
    try {
      const res = await Api.get(`${endpoints["employeeFromUser"]}/${userId}`);
      setEmployeeId(res.data);
    } catch (err) {
      console.error("Không thể lấy employeeId từ userId:", err);
    }
  };

  useEffect(() => {
    loadCompanyImages();
  }, [companyId]);

  useEffect(() => {
    const company = companyData?.[0]?.companyId || {};
    const employerId = company.employerId;

    if (employerId) {
      loadFollowerCount(employerId);
    }

    if (user?.role === "ROLE_EMPLOYEE" && user?.id) {
      loadEmployeeId(user.id);
    }
  }, [companyData, user]);

  const handleToggleEditForm = async () => {
    if (showEditForm) {
      try {
        await authApis().put(`${endpoints["company_info"]}/${companyId}`, formData);
        toast.success("Cập nhật thông tin công ty thành công!");
        setShowEditForm(false);
        await loadCompanyImages();
      } catch (err) {
        console.error("Error updating company info:", err);
        toast.error("Cập nhật thông tin công ty thất bại!");
      }
    } else {
      setShowEditForm(true);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleUpdateImage = async (event) => {
    event.preventDefault();
    const formData = new FormData(event.target);
    formData.append("companyId", companyId);

    try {
      await authApis().post(endpoints["company_images"], formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      toast.success("Tải ảnh lên thành công!");
      await loadCompanyImages();
    } catch (err) {
      console.error("Error uploading image:", err);
      toast.error("Tải ảnh lên thất bại!");
    }
  };

  const handleDeleteImage = async (imageId) => {
    try {
      await authApis().delete(`${endpoints["company_images"]}/${companyId}/${imageId}`);
      toast.success("Xóa ảnh thành công!");
      await loadCompanyImages();
    } catch (err) {
      console.error("Error deleting image:", err);
      toast.error("Xóa ảnh thất bại!");
    }
  };

  if (!companyData || companyData.length === 0) {
    return <p className="text-center mt-5">Không có dữ liệu công ty hoặc ảnh.</p>;
  }

  const company = companyData[0].companyId || {};
  const isOwner = user && user.role === "ROLE_EMPLOYER" && user.id === company.userId;

  return (
    <Container className="mt-5">
      <ToastContainer />
      <Card className="shadow p-4">
        <h3 className="mb-4 text-center">Thông Tin Công Ty</h3>
        <Row>
          <Col md={4} className="image-list-container">
            <h5 className="mb-3 text-center">Ảnh Công Ty</h5>
            <div className="image-list">
              {companyData.map((item) => (
                <div key={item.id} className="image-item">
                  <img
                    src={item.imagePath || "/default-company-logo.png"}
                    alt={item.caption || "Ảnh công ty"}
                    className="img-fluid rounded mb-3"
                  />
                  <p className="text-center">{item.caption || "Không có chú thích"}</p>
                  {isOwner && (
                    <div className="delete-icon" onClick={() => handleDeleteImage(item.id)}>
                      🗑️
                    </div>
                  )}
                </div>
              ))}
            </div>

            {isOwner && (
              <Form onSubmit={handleUpdateImage} className="mt-3">
                <Form.Group className="mb-3">
                  <Form.Label>Chọn ảnh mới</Form.Label>
                  <Form.Control type="file" name="file" />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Chú thích</Form.Label>
                  <Form.Control type="text" name="caption" placeholder="Nhập chú thích" />
                </Form.Group>
                <Button variant="primary" type="submit">Tải ảnh lên</Button>
              </Form>
            )}
          </Col>

          <Col md={6}>
            <Row>
              <Col md={12} className="company-info">
                <p><strong>🏢 Tên công ty:</strong> {company.name || "Chưa cập nhật"}</p>
              </Col>
            </Row>
            <Row>
              <Col md={12} className="company-info">
                <p><strong>📍 Địa chỉ:</strong> {company.address || "Chưa cập nhật"}</p>
              </Col>
            </Row>
            <Row>
              <Col md={12} className="company-info">
                <p><strong>📜 Mã thuế:</strong> {company.taxCode || "Chưa cập nhật"}</p>
              </Col>
            </Row>

            {isOwner && (
              <Row className="mt-3">
                <Col md={12} className="button-container">
                  <Button variant="primary" onClick={handleToggleEditForm} className="me-2">
                    {showEditForm ? "Lưu" : "Cập nhật thông tin công ty"}
                  </Button>
                  <Button variant="secondary" onClick={() => document.querySelector("form").scrollIntoView({ behavior: "smooth" })}>
                    Cập nhật ảnh
                  </Button>
                </Col>
              </Row>
            )}

            {showEditForm && (
              <div className="edit-form slide-down">
                <Form>
                  <Form.Group className="mb-3">
                    <Form.Label>Tên công ty</Form.Label>
                    <Form.Control type="text" name="name" value={formData.name} onChange={handleInputChange} />
                  </Form.Group>
                  <Form.Group className="mb-3">
                    <Form.Label>Địa chỉ</Form.Label>
                    <Form.Control type="text" name="address" value={formData.address} onChange={handleInputChange} />
                  </Form.Group>
                  <Form.Group className="mb-3">
                    <Form.Label>Mã thuế</Form.Label>
                    <Form.Control type="text" name="taxCode" value={formData.taxCode} onChange={handleInputChange} />
                  </Form.Group>
                </Form>
              </div>
            )}
          </Col>

          <Col md={2} className="d-flex flex-column align-items-center">
            {user?.role === "ROLE_EMPLOYEE" && employeeId && company?.employerId && (
              <>
                <FollowButton
                  employeeId={employeeId}
                  employerId={company.employerId}
                  onChange={(status) => {
                    setFollowerCount((prev) => status ? prev + 1 : Math.max(prev - 1, 0));
                  }}
                />
                <p className="mt-2 small text-muted">{followerCount} người đã follow</p>
              </>
            )}
          </Col>
        </Row>
      </Card>
    </Container>
  );
};

export default CompanyInfo;

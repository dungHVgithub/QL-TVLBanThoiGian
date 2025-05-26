import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Container, Card, Row, Col, Button, Form } from "react-bootstrap";
import Api, { authApis, endpoints } from "../configs/Api";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "../static/companyInfo.css"; // Import file CSS

const CompanyInfo = () => {
  const { companyId } = useParams();
  const [companyData, setCompanyData] = useState(null);
  const [showEditForm, setShowEditForm] = useState(false); // Trạng thái hiển thị form chỉnh sửa
  const [formData, setFormData] = useState({ name: "", address: "", taxCode: "" }); // Dữ liệu form

  // Hàm tải dữ liệu công ty và ảnh
  const loadCompanyImages = async () => {
    try {
      const res = await Api.get(`${endpoints["company_images"]}/${companyId}`);
      console.log("Company Images Data:", res.data);
      setCompanyData(res.data); // Lưu toàn bộ mảng dữ liệu vào state

      // Khởi tạo dữ liệu form từ thông tin công ty đầu tiên
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

  useEffect(() => {
    loadCompanyImages();
  }, [companyId]);

  // Xử lý khi bấm nút "Cập nhật thông tin công ty" hoặc "Lưu"
  const handleToggleEditForm = async () => {
    if (showEditForm) {
      // Gửi PUT request khi bấm "Lưu"
      try {
        const response = await authApis().put(`${endpoints["company_info"]}/${companyId}`, formData);
        console.log("Update success:", response.data);
        toast.success("Cập nhật thông tin công ty thành công!");
        setShowEditForm(false); // Đóng form sau khi cập nhật thành công
        await loadCompanyImages(); // Tải lại dữ liệu để cập nhật giao diện
      } catch (err) {
        console.error("Error updating company info:", err);
        toast.error("Cập nhật thông tin công ty thất bại!");
      }
    } else {
      setShowEditForm(true);
    }
  };

  // Xử lý thay đổi dữ liệu trong form
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  // Xử lý khi bấm nút "Cập nhật ảnh"
  const handleUpdateImage = async (event) => {
    event.preventDefault();
    const formData = new FormData(event.target);
    formData.append("companyId", companyId); // Thêm companyId vào form data

    try {
      const response = await authApis().post(endpoints["company_images"], formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      console.log("Image uploaded:", response.data);
      toast.success("Tải ảnh lên thành công!");
      await loadCompanyImages(); // Cập nhật lại danh sách ảnh sau khi upload
    } catch (err) {
      console.error("Error uploading image:", err);
      toast.error("Tải ảnh lên thất bại!");
    }
  };

  // Xử lý xóa ảnh
  const handleDeleteImage = async (imageId) => {
    try {
      await authApis().delete(`${endpoints["company_images"]}/${companyId}/${imageId}`);
      toast.success("Xóa ảnh thành công!");
      await loadCompanyImages(); // Cập nhật lại danh sách ảnh sau khi xóa
    } catch (err) {
      console.error("Error deleting image:", err);
      toast.error("Xóa ảnh thất bại!");
    }
  };

  if (!companyData || companyData.length === 0) {
    return <p className="text-center mt-5">Không có dữ liệu công ty hoặc ảnh.</p>;
  }

  // Lấy thông tin công ty từ đối tượng đầu tiên (tất cả đối tượng có cùng companyId)
  const company = companyData[0].companyId || {};

  return (
    <Container className="mt-5">
      <ToastContainer />
      <Card className="shadow p-4">
        <h3 className="mb-4 text-center">Thông Tin Công Ty</h3>
        <Row>
          {/* Cột trái: Danh sách ảnh (30%) */}
          <Col md={4} className="image-list-container">
            <h5 className="mb-3 text-center">Ảnh Công Ty</h5>
            <div className="image-list">
              {companyData.map((item) => (
                <div
                  key={item.id}
                  className="image-item"
                  onMouseEnter={(e) => e.currentTarget.classList.add("hover")}
                  onMouseLeave={(e) => e.currentTarget.classList.remove("hover")}
                >
                  <img
                    src={item.imagePath || "/default-company-logo.png"}
                    alt={item.caption || "Ảnh công ty"}
                    className="img-fluid rounded mb-3"
                  />
                  <p className="text-center">{item.caption || "Không có chú thích"}</p>
                  <div className="delete-icon" onClick={() => handleDeleteImage(item.id)}>
                    🗑️
                  </div>
                </div>
              ))}
            </div>
            {/* Form để upload ảnh mới */}
            <Form onSubmit={handleUpdateImage} className="mt-3">
              <Form.Group className="mb-3">
                <Form.Label>Chọn ảnh mới</Form.Label>
                <Form.Control type="file" name="file" />
              </Form.Group>
              <Form.Group className="mb-3">
                <Form.Label>Chú thích</Form.Label>
                <Form.Control type="text" name="caption" placeholder="Nhập chú thích" />
              </Form.Group>
              <Button variant="primary" type="submit">
                Tải ảnh lên
              </Button>
            </Form>
          </Col>

          {/* Cột phải: Thông tin công ty (70%) */}
          <Col md={7}>
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

            {/* Nút "Cập nhật thông tin công ty" và "Cập nhật ảnh" */}
            <Row className="mt-3">
              <Col md={12} className="button-container">
                <Button
                  variant="primary"
                  onClick={handleToggleEditForm}
                  className="me-2"
                >
                  {showEditForm ? "Lưu" : "Cập nhật thông tin công ty"}
                </Button>
                <Button
                  variant="secondary"
                  onClick={() => document.querySelector("form").scrollIntoView({ behavior: "smooth" })}
                >
                  Cập nhật ảnh
                </Button>
              </Col>
            </Row>
            

            {/* Popup form chỉnh sửa thông tin công ty */}
            {showEditForm && (
              <div className={`edit-form ${showEditForm ? "slide-down" : ""}`}>
                <Form>
                  <Form.Group className="mb-3">
                    <Form.Label>Tên công ty</Form.Label>
                    <Form.Control
                      type="text"
                      name="name"
                      value={formData.name}
                      onChange={handleInputChange}
                      placeholder="Nhập tên công ty"
                    />
                  </Form.Group>
                  <Form.Group className="mb-3">
                    <Form.Label>Địa chỉ</Form.Label>
                    <Form.Control
                      type="text"
                      name="address"
                      value={formData.address}
                      onChange={handleInputChange}
                      placeholder="Nhập địa chỉ"
                    />
                  </Form.Group>
                  <Form.Group className="mb-3">
                    <Form.Label>Mã thuế</Form.Label>
                    <Form.Control
                      type="text"
                      name="taxCode"
                      value={formData.taxCode}
                      onChange={handleInputChange}
                      placeholder="Nhập mã thuế"
                    />
                  </Form.Group>
                </Form>
              </div>
            )}


          </Col>
          <Col md={1}>
                <Button
                  variant="primary"
                  className="follow-button-fixed"
                  onClick={() => toast.success("Bạn đã follow công ty này!")}
                >
                  Follow
                </Button>
              </Col>

        </Row>
      </Card>
    </Container>
  );
};

export default CompanyInfo;
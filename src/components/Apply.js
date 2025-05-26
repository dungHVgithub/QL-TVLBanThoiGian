import { useEffect, useState, useContext } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Api, { authApis, endpoints } from "../configs/Api";
import { MyUserContext } from "../configs/MyContexts";
import { Button, Alert } from "react-bootstrap";
import "../static/Apply.css";

const Apply = () => {
  const { employeeId, jobId } = useParams();
  const navigate = useNavigate();
  const user = useContext(MyUserContext);
  const [msg, setMsg] = useState("");
  const [loading, setLoading] = useState(false);
  const [jobPosting, setJobPosting] = useState(null);
  const [formData, setFormData] = useState({
    documentType: "CV",
    file: null,
    name: "",
  });

  useEffect(() => {
    const fetchJobPosting = async () => {
      try {
        setLoading(true);
        const res = await Api.get(`${endpoints.job_postings}/${jobId}`);
        setJobPosting(res.data);
      } catch (error) {
        console.error("Error fetching job posting:", error);
        setMsg("❌ Không thể tải thông tin công việc. Vui lòng thử lại!");
      } finally {
        setLoading(false);
      }
    };
    fetchJobPosting();
  }, [jobId]);

  const handleApplySubmit = async () => {
    if (!user || !user.id) {
      setMsg("❌ Vui lòng đăng nhập để ứng tuyển!");
      setTimeout(() => navigate("/login"), 2000);
      return;
    }

    if (!formData.file) {
      setMsg("❌ Vui lòng chọn một file để tải lên!");
      return;
    }

    if (!formData.name.trim()) {
      setMsg("❌ Vui lòng nhập tên tài liệu!");
      return;
    }

    try {
      setLoading(true);
      const formDataToSend = new FormData();
      formDataToSend.append("employeeId", employeeId);
      formDataToSend.append("documentType", formData.documentType);
      formDataToSend.append("file", formData.file);
      formDataToSend.append("jobId", jobId);
      formDataToSend.append("name", formData.name);

      const res = await authApis().post(
        `${endpoints.addDocumentForEmployee.replace("{employeeId}", employeeId)}`,
        formDataToSend,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );

      if (res.status === 201) {
        setMsg("✅ Ứng tuyển và tải tài liệu thành công!");
        setTimeout(() => navigate("/"), 2000);
      }
    } catch (error) {
      console.error("Error applying for job or uploading document:", error);
      if (error.response) {
        setMsg(`❌ Ứng tuyển thất bại: ${error.response.data.message || "Vui lòng thử lại!"}`);
      } else {
        setMsg("❌ Ứng tuyển hoặc tải tài liệu thất bại. Vui lòng thử lại!");
      }
    } finally {
      setLoading(false);
    }
  };

  const handleFormChange = (e) => {
    const { name, value, files } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: files ? files[0] : value,
    }));
  };

  return (
    <div className="apply-wrapper">
      <h1 className="apply-heading">ỨNG TUYỂN CÔNG VIỆC</h1>
      {msg && (
        <Alert variant={msg.startsWith("✅") ? "success" : "danger"}>{msg}</Alert>
      )}
      {loading ? (
        <div className="apply-loading">Đang tải...</div>
      ) : (
        <div className="apply-layout">
          {/* Form bên trái (40%) */}
          <div className="apply-form-container">
            <h3 className="apply-form-title">Thông tin ứng tuyển</h3>
            <form id="apply-form">
              <div className="apply-form-group">
                <label htmlFor="apply-name">Tên tài liệu</label>
                <input
                  type="text"
                  id="apply-name"
                  name="name"
                  value={formData.name}
                  onChange={handleFormChange}
                  className="apply-form-control"
                  placeholder="Nhập tên tài liệu"
                  required
                />
              </div>
              <div className="apply-form-group">
                <label htmlFor="apply-document-type">Loại tài liệu</label>
                <select
                  id="apply-document-type"
                  name="documentType"
                  value={formData.documentType}
                  onChange={handleFormChange}
                  className="apply-form-control"
                >
                  <option value="CV">CV</option>
                  <option value="Diploma">Diploma</option>
                </select>
              </div>
              <div className="apply-form-group">
                <label htmlFor="apply-file-upload">Tải lên tài liệu</label>
                <input
                  type="file"
                  id="apply-file-upload"
                  name="file"
                  onChange={handleFormChange}
                  className="apply-form-control"
                  required
                />
              </div>
              <Button
                variant="success"
                onClick={handleApplySubmit}
                disabled={loading}
                className="apply-submit-btn"
              >
                Xác nhận ứng tuyển
              </Button>
            </form>
          </div>

          {/* Thông tin jobPosting bên phải (60%) */}
          <div className="apply-job-info-container">
            <h3 className="apply-job-info-title">Thông tin công việc</h3>
            {jobPosting ? (
              <div className="apply-job-details">
                <p>
                  <strong>Tên công việc:</strong>{" "}
                  {jobPosting.name || "Chưa xác định"}
                </p>
                <p>
                  <strong>Lương:</strong>{" "}
                  {jobPosting.salary ? `${jobPosting.salary} $` : "Thỏa thuận"}
                </p>
                <p>
                  <strong>Địa chỉ:</strong>{" "}
                  {jobPosting.employerId?.company?.address || "Chưa xác định"}
                </p>
                <p>
                  <strong>Mã số thuế:</strong>{" "}
                  {jobPosting.employerId?.company?.taxCode || "Chưa xác định"}
                </p>
              </div>
            ) : (
              <p>Không có thông tin công việc.</p>
            )}
          </div>
        </div>
      )}
    </div>
  );
};

export default Apply;
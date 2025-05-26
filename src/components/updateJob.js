import { useEffect, useState, useContext } from "react";
import { Alert, Button, FloatingLabel, Form, Col, Row } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";
import { authApis, endpoints } from "../configs/Api";
import MySpinner from "./layouts/MySpinner";
import "../static/UpdateJob.css"; // Import CSS tùy chỉnh
import { MyUserContext } from "../configs/MyContexts";
import { toast } from 'react-toastify';

const UpdateJob = () => {
  const { jobId } = useParams();
  const [job, setJob] = useState({});
  const [jobDetail, setJobDetail] = useState({});
  const [msg, setMsg] = useState("");
  const [loading, setLoading] = useState(false);
  const [categories, setCategories] = useState([]);
  const user = useContext(MyUserContext);
  const nav = useNavigate();
  const [employerId, setEmployerId] = useState(null);

  const info = [
    { label: "Tên công việc", type: "text", field: "name", required: true, target: "job", icon: "💼" },
    { label: "Mô tả công việc", type: "text", field: "description", required: true, target: "jobDetail", icon: "📝" },
    { label: "Cấp độ", type: "text", field: "level", required: true, target: "jobDetail", icon: "📊" },
    { label: "Kinh nghiệm (năm)", type: "text", field: "experience", required: true, target: "jobDetail", icon: "🎯" },
    { label: "Ngày hết hạn ứng tuyển", type: "date", field: "submitEnd", required: true, target: "jobDetail", icon: "📅" },
    { label: "Phúc lợi", type: "text", field: "benefit", required: true, target: "jobDetail", icon: "🎁" },
    { label: "Lương ($)", type: "number", field: "salary", required: true, target: "job", icon: "💰" },
    { label: "Giờ bắt đầu", type: "time", field: "timeStart", required: true, target: "job", icon: "🕐" },
    { label: "Giờ kết thúc", type: "time", field: "timeEnd", required: true, target: "job", icon: "🕕" },
  ];

  const setState = (value, field) => {
    console.log(`Setting ${field} to:`, value);
    const fieldInfo = info.find(f => f.field === field);
    
    if (field === "categoryId") {
      setJob(prev => {
        const newJob = { ...prev, [field]: value };
        console.log("Updated job state:", newJob);
        return newJob;
      });
    } else if (fieldInfo) {
      if (fieldInfo.target === "job") {
        setJob(prev => {
          const newJob = { ...prev, [field]: value === "" ? "" : value };
          console.log("Updated job state:", newJob);
          return newJob;
        });
      } else if (fieldInfo.target === "jobDetail") {
        setJobDetail(prev => {
          const newJobDetail = { ...prev, [field]: value === "" ? "" : value };
          console.log("Updated jobDetail state:", newJobDetail);
          return newJobDetail;
        });
      }
    }
  };

  const loadCategories = async () => {
    try {
      const res = await authApis().get(endpoints.categories);
      setCategories(res.data);
      console.log("Loaded categories:", res.data);
    } catch (error) {
      console.error("Error loading categories:", error);
      setMsg("❌ Không thể tải danh mục công việc!");
    }
  };

  const loadEmployerId = async () => {
    try {
      const res = await authApis().get(endpoints.employers);
      const employers = res.data;
      const matchingEmployer = employers.find(emp => emp.userId.id === user.id);
      if (matchingEmployer) {
        setEmployerId(matchingEmployer.id);
      } else {
        setMsg("❌ Không tìm thấy thông tin Employer cho người dùng này!");
        setTimeout(() => nav("/login"), 2000);
      }
    } catch (error) {
      console.error("Error loading employerId:", error);
      setMsg("❌ Không thể tải thông tin Employer!");
    }
  };

  const formatTimeForInput = (timeString) => {
    if (!timeString) return "";
    if (/^\d{2}:\d{2}$/.test(timeString)) return timeString;
    try {
      const date = new Date(timeString);
      if (isNaN(date.getTime())) return "";
      return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', hour12: false });
    } catch {
      return "";
    }
  };

  const loadJobData = async () => {
    try {
      setLoading(true);
      const jobRes = await authApis().get(`${endpoints['job_postings']}/${jobId}`);
      const jobData = jobRes.data;
      console.log("Job data:", jobData);

      setJob({
        id: jobData.id,
        name: jobData.name || "",
        salary: jobData.salary || "",
        timeStart:jobData.timeStart,
        timeEnd:jobData.timeEnd,
        categoryId: "",
        state: jobData.state || "pending",
      });

      const detailRes = await authApis().get(`${endpoints.job_details}/jobPosting/${jobId}`);
      const detailData = detailRes.data;
      setJobDetail({
        id: detailData.id || 0,
        description: detailData.description || "",
        level: detailData.level || "",
        experience: detailData.experience || "",
        submitEnd: detailData.submitEnd ? new Date(detailData.submitEnd).toISOString().split('T')[0] : "",
        benefit: detailData.benefit || "",
      });
      setLoading(false);
    } catch (error) {
      console.error("Error loading job data:", error);
      setMsg(`❌ Không thể tải thông tin công việc! Lỗi: ${error.message}`);
      setLoading(false);
    }
  };

  useEffect(() => {
    if (user && user.id && jobId) {
      loadCategories();
      loadEmployerId();
      loadJobData();
    } else {
      setMsg("❌ Vui lòng đăng nhập để cập nhật tin tuyển dụng!");
      setTimeout(() => nav("/login"), 2000);
    }
  }, [user, nav, jobId]);

  const updateJob = async (e) => {
    e.preventDefault();

    if (!user || !user.id) {
      setMsg("❌ Vui lòng đăng nhập để cập nhật tin tuyển dụng!");
      setTimeout(() => nav("/login"), 2000);
      return;
    }

    if (!employerId) {
      setMsg("❌ Không thể xác định Employer. Vui lòng thử lại!");
      return;
    }

    // Validation thủ công
    const requiredFields = info.filter(f => f.required);
    for (const f of requiredFields) {
      const value = job[f.field] || jobDetail[f.field] || "";
      if (!value) {
        setMsg(`❌ Vui lòng điền ${f.label}!`);
        return;
      }
    }

    if (!job.categoryId) {
      setMsg("❌ Vui lòng chọn danh mục công việc!");
      return;
    }

    try {
      setLoading(true);

      const jobPosting = {
        name: job.name,
        salary: job.salary ? parseFloat(job.salary) : 0,
        timeStart: job.timeStart,
        timeEnd: job.timeEnd,
        categoryId: parseInt(job.categoryId),
        employerId: employerId,
        state: job.state || "pending",
      };

      const jobDetails = {
        id: jobDetail.id,
        description: jobDetail.description,
        level: jobDetail.level,
        experience: jobDetail.experience,
        submitEnd: jobDetail.submitEnd,
        benefit: jobDetail.benefit,
        jobPosting: { id: parseInt(jobId) },
      };

      console.log("Sending jobPosting:", jobPosting);
      console.log("Sending jobDetails:", jobDetails);

      await authApis().put(`${endpoints['job_postings']}/${jobId}`, jobPosting);
      const response = await authApis().put(`${endpoints.job_details}/jobPosting/${jobId}`, jobDetails);
      console.log("PUT jobDetails response:", response.data);

      toast.success("Cập nhật bài đăng thành công!");
      setTimeout(() => nav(`/postList/${employerId}`), 2000);
    } catch (err) {
      console.error("Cập nhật thất bại:", err);
      toast.error("Có lỗi xảy ra khi cập nhật bài đăng. Vui lòng thử lại! Chi tiết: " + err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="update-job-container">
      <div className="container">
        <div className="update-job-main-card">
          <div className="update-job-header">
            <h1 className="update-job-title">✨ Cập Nhật Tin Tuyển Dụng</h1>
            <p className="update-job-subtitle">Chỉnh sửa thông tin tuyển dụng của bạn</p>
          </div>

          {msg && (
            <div className="px-4">
              <Alert 
                variant={msg.startsWith("✅") ? "success" : "danger"} 
                className="update-job-alert"
              >
                {msg}
              </Alert>
            </div>
          )}

          {loading || !job.id ? (
            <div className="update-job-spinner-container">
              <MySpinner />
              <div className="update-job-spinner-text">Đang tải thông tin công việc...</div>
            </div>
          ) : (
            <Form onSubmit={updateJob} className="update-job-form">
              <div className="update-job-section-title">
                📋 Thông tin cơ bản
              </div>
              
              <Row>
                {info.slice(0, 3).map((f) => (
                  <Col md={6} key={f.field} className="update-job-form-group">
                    <FloatingLabel controlId={`floating${f.field}`} label={`${f.icon} ${f.label}`}>
                      <Form.Control
                        type={f.type}
                        placeholder={f.label}
                        value={job[f.field] ?? (jobDetail[f.field] || "")}
                        onChange={(e) => setState(e.target.value, f.field)}
                      />
                    </FloatingLabel>
                  </Col>
                ))}
                
                <Col md={6} className="update-job-form-group">
                  <FloatingLabel controlId="floatingCategoryId" label="🏷️ Danh mục công việc">
                    <Form.Select
                      value={job.categoryId || ""}
                      onChange={(e) => setState(e.target.value, "categoryId")}
                    >
                      <option value="">Chọn danh mục</option>
                      {categories.map((c) => (
                        <option key={c.id} value={c.id.toString()}>
                          {c.name}
                        </option>
                      ))}
                    </Form.Select>
                  </FloatingLabel>
                </Col>
              </Row>

              <div className="update-job-section-title">
                💼 Chi tiết công việc
              </div>
              
              <Row>
                {info.slice(3, 6).map((f) => (
                  <Col md={6} key={f.field} className="update-job-form-group">
                    <FloatingLabel controlId={`floating${f.field}`} label={`${f.icon} ${f.label}`}>
                      <Form.Control
                        type={f.type}
                        placeholder={f.label}
                        value={job[f.field] ?? (jobDetail[f.field] || "")}
                        onChange={(e) => setState(e.target.value, f.field)}
                      />
                    </FloatingLabel>
                  </Col>
                ))}
              </Row>

              <div className="update-job-section-title">
                ⏰ Thời gian & Lương
              </div>
              
              <Row>
                {info.slice(6).map((f) => (
                  <Col md={4} key={f.field} className="update-job-form-group">
                    <FloatingLabel controlId={`floating${f.field}`} label={`${f.icon} ${f.label}`}>
                      <Form.Control
                        type={f.type}
                        placeholder={f.label}
                        value={job[f.field] ?? (jobDetail[f.field] || "")}
                        onChange={(e) => setState(e.target.value, f.field)}
                      />
                    </FloatingLabel>
                  </Col>
                ))}
              </Row>

              <div className="update-job-submit-container">
                <Button 
                  type="submit" 
                  className="update-job-submit-btn"
                  disabled={loading}
                >
                  {loading ? "Đang cập nhật..." : "🚀 Cập nhật ngay"}
                </Button>
              </div>
            </Form>
          )}
        </div>
      </div>
    </div>
  );
};

export default UpdateJob;
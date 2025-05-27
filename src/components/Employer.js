import { useEffect, useState, useContext } from "react";
import { Alert, Button, FloatingLabel, Form, Col, Row } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import Api, { authApis, endpoints } from "../configs/Api";
import MySpinner from "./layouts/MySpinner";
import "../static/employer.css";
import { MyUserContext, MyDispatchContext } from "../configs/MyContexts";
import cookie from "react-cookies";

const Employer = () => {
  const info = [
    { label: "Tên công việc", type: "text", field: "name" },
    { label: "Mô tả công việc", type: "text", field: "description" },
    { label: "Cấp độ", type: "text", field: "level" },
    { label: "Kinh nghiệm (năm)", type: "text", field: "experience" },
    { label: "Ngày hết hạn ứng tuyển", type: "date", field: "submitEnd" },
    { label: "Phúc lợi", type: "text", field: "benefit" },
    { label: "Lương ($)", type: "number", field: "salary" },
    { label: "Giờ bắt đầu", type: "time", field: "timeStart" },
    { label: "Giờ kết thúc", type: "time", field: "timeEnd" },
  ];

  const [job, setJob] = useState({});
  const [msg, setMsg] = useState("");
  const [loading, setLoading] = useState(false);
  const [categories, setCategories] = useState([]);
  const user = useContext(MyUserContext);
  const dispatch = useContext(MyDispatchContext);
  const nav = useNavigate();
  const [employerId, setEmployerId] = useState(null);
  const [isLoadingUser, setIsLoadingUser] = useState(true);

  const setState = (value, field) => {
    setJob({ ...job, [field]: value });
  };

  const loadCategories = async () => {
    try {
      const res = await Api.get(endpoints.categories);
      setCategories(res.data);
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
      if (error.response && error.response.status === 401) {
        setMsg("❌ Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại!");
        setTimeout(() => nav("/login"), 2000);
      } else {
        setMsg("❌ Không thể tải thông tin Employer!");
      }
    }
  };

  useEffect(() => {
   
    if (isLoadingUser) {
      const token = cookie.load("token") || localStorage.getItem("token");
      if (token && !user) {
        const loadUserProfile = async () => {
          try {
            const res = await authApis().get(endpoints.profile);
            console.log("Profile data:", res.data);
            dispatch({
              type: "login",
              payload: {
                id: res.data.id,
                token: token,
                username: res.data.username,
                name: res.data.name,
                role: res.data.role
              }
            });
          } catch (err) {
            console.error("Error loading profile:", err);
            setMsg("❌ Không thể tải thông tin người dùng!");
            setTimeout(() => nav("/login"), 2000);
          } finally {
            setIsLoadingUser(false);
          }
        };
        loadUserProfile();
      } else {
        setIsLoadingUser(false);
      }
    } else if (user && user.id) {
      if (user.role !== "ROLE_EMPLOYER") {
        setMsg("❌ Bạn không có quyền truy cập trang này!");
        setTimeout(() => nav("/"), 2000);
        return;
      }
      loadCategories();
      loadEmployerId();
    } else {
      setMsg("❌ Vui lòng đăng nhập để đăng tin tuyển dụng!");
      setTimeout(() => nav("/login"), 2000);
    }
  }, [user, nav, isLoadingUser, dispatch]);

  const submitJob = async (e) => {
    e.preventDefault();

    if (!user || !user.id) {
      setMsg("❌ Vui lòng đăng nhập để đăng tin tuyển dụng!");
      setTimeout(() => nav("/login"), 2000);
      return;
    }

    if (!employerId) {
      setMsg("❌ Không thể xác định Employer. Vui lòng thử lại!");
      setTimeout(() => nav("/login"), 2000);
      return;
    }

    try {
      setLoading(true);

      const jobPosting = {
        name: job.name,
        salary: parseFloat(job.salary),
        timeStart: job.timeStart, // Chuỗi "HH:mm"
        timeEnd: job.timeEnd, // Chuỗi "HH:mm"
        categoryId: parseInt(job.categoryId),
        employerId: employerId,
        state: "pending"
      };

      console.log("Sending job posting:", jobPosting);

      const jobPostingRes = await authApis().post(endpoints.job_postings, jobPosting);
      const jobPostingId = jobPostingRes.data.id;

      const jobDetails = {
        description: job.description,
        level: job.level,
        experience: job.experience,
        submitEnd: new Date(job.submitEnd).getTime(),
        benefit: job.benefit,
        jobPosting: { id: jobPostingId },
      };

      const jobDetailsRes = await authApis().post(endpoints.job_details, jobDetails);

      if (jobDetailsRes.status === 201) {
        setMsg("✅ Đăng tin tuyển dụng thành công!");
        setTimeout(() => nav("/employer"), 2000);
      }
    } catch (err) {
      console.error("Đăng tin thất bại:", err);
      if (err.response && err.response.status === 405) {
        setMsg("❌ Server không hỗ trợ phương thức này. Vui lòng kiểm tra cấu hình backend!");
      } else if (err.response && err.response.status === 400) {
        setMsg("❌ Dữ liệu không hợp lệ. Vui lòng kiểm tra các trường nhập!");
      } else {
        setMsg("❌ Đăng tin thất bại. Vui lòng thử lại!");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="employer-container">
      <div className="employer-main-content">
        <h1 className="employer-title">ĐĂNG TIN TUYỂN DỤNG</h1>

        {msg && (
          <Alert 
            variant={msg.startsWith("✅") ? "success" : "danger"}
            className={`employer-alert ${msg.startsWith("✅") ? "alert-success" : "alert-danger"}`}
          >
            {msg}
          </Alert>
        )}

        {isLoadingUser ? (
          <div className="employer-spinner-container">
            <div className="employer-loading-spinner"></div>
          </div>
        ) : (
          <Form onSubmit={submitJob} className="employer-form-container">
            <Row className="employer-form-row">
              {info.map((f) => (
                <Col md={6} key={f.field} className="employer-form-col">
                  <div className="employer-form-group">
                    <FloatingLabel 
                      controlId={`floating${f.field}`} 
                      label={f.label}
                      className="employer-floating-label"
                    >
                      <Form.Control
                        type={f.type}
                        placeholder={f.label}
                        required
                        value={job[f.field] || ""}
                        onChange={(e) => setState(e.target.value, f.field)}
                        className="employer-form-control"
                      />
                    </FloatingLabel>
                  </div>
                </Col>
              ))}
              <Col md={6} className="employer-form-col">
                <div className="employer-form-group">
                  <FloatingLabel 
                    controlId="floatingCategoryId" 
                    label="Danh mục công việc"
                    className="employer-floating-label"
                  >
                    <Form.Select
                      required
                      value={job.categoryId || ""}
                      onChange={(e) => setState(e.target.value, "categoryId")}
                      className="employer-form-select"
                    >
                      <option value="">Chọn danh mục</option>
                      {categories.map((c) => (
                        <option key={c.id} value={c.id}>
                          {c.name}
                        </option>
                      ))}
                    </Form.Select>
                  </FloatingLabel>
                </div>
              </Col>
            </Row>

            <div className="employer-submit-section">
              {loading ? (
                <div className="employer-spinner-container">
                  <div className="employer-loading-spinner"></div>
                </div>
              ) : (
                <Button 
                  type="submit" 
                  size="lg" 
                  className="employer-submit-btn"
                >
                  Đăng tin
                </Button>
              )}
            </div>
          </Form>
        )}
      </div>
    </div>
  );
};

export default Employer;
import { useEffect, useState, useContext } from "react";
import { Alert, Button, FloatingLabel, Form, Col, Row } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";
import { authApis, endpoints } from "../configs/Api";
import MySpinner from "./layouts/MySpinner";
import "../static/employer.css";
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
    { label: "Tên công việc", type: "text", field: "name", required: true },
    { label: "Mô tả công việc", type: "text", field: "description", required: true },
    { label: "Cấp độ", type: "text", field: "level", required: true },
    { label: "Kinh nghiệm (năm)", type: "text", field: "experience", required: true },
    { label: "Ngày hết hạn ứng tuyển", type: "date", field: "submitEnd", required: true },
    { label: "Phúc lợi", type: "text", field: "benefit", required: true },
    { label: "Lương ($)", type: "number", field: "salary", required: true },
    { label: "Giờ bắt đầu", type: "time", field: "timeStart", required: true },
    { label: "Giờ kết thúc", type: "time", field: "timeEnd", required: true },
  ];

  const setState = (value, field) => {
    console.log(`Setting ${field} to:`, value);
    if (field === "categoryId") {
      setJob(prev => {
        const newJob = { ...prev, [field]: value };
        console.log("Updated job state:", newJob);
        return newJob;
      });
    } else if (info.some(f => f.field === field)) {
      setJob(prev => {
        const newJob = { ...prev, [field]: value === "" ? "" : value };
        console.log("Updated job state:", newJob);
        return newJob;
      });
    } else {
      setJobDetail(prev => {
        const newJobDetail = { ...prev, [field]: value === "" ? "" : value };
        console.log("Updated jobDetail state:", newJobDetail);
        return newJobDetail;
      });
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
        timeStart: formatTimeForInput(jobData.timeStart),
        timeEnd: formatTimeForInput(jobData.timeEnd),
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
        submitEnd: new Date(jobDetail.submitEnd).getTime(),
        benefit: jobDetail.benefit,
        jobPosting: { id: parseInt(jobId) },
      };

      console.log("Sending jobPosting:", jobPosting);
      console.log("Sending jobDetails:", jobDetails);

      await authApis().put(`${endpoints['job_postings']}/${jobId}`, jobPosting);
      await authApis().put(`${endpoints.job_details}/${jobDetail.id}`, jobDetails);

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
    <div className="container mt-4">
      <h1 className="text-center text-success mb-4">CẬP NHẬT TIN TUYỂN DỤNG</h1>

      {msg && <Alert variant={msg.startsWith("✅") ? "success" : "danger"}>{msg}</Alert>}

      {loading || !job.id ? (
        <MySpinner />
      ) : (
        <Form onSubmit={updateJob} className="bg-light p-4 rounded shadow">
          <Row>
            {info.map((f) => (
              <Col md={6} key={f.field} className="mb-3">
                <FloatingLabel controlId={`floating${f.field}`} label={f.label}>
                  <Form.Control
                    type={f.type}
                    placeholder={f.label}
                    value={job[f.field] ?? jobDetail[f.field] ?? ""}
                    onChange={(e) => setState(e.target.value, f.field)}
                  />
                </FloatingLabel>
              </Col>
            ))}
            <Col md={6} className="mb-3">
              <FloatingLabel controlId="floatingCategoryId" label="Danh mục công việc">
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

          <div className="text-center">
            <Button type="submit" variant="success" size="lg" className="px-5">
              Cập nhật
            </Button>
          </div>
        </Form>
      )}
    </div>
  );
};

export default UpdateJob;
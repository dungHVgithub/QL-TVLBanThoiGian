import { useEffect, useState, useContext } from "react";
import { useParams } from "react-router-dom";
import Api, { authApis, endpoints } from "../configs/Api";
import { MyUserContext } from "../configs/MyContexts";
import { Container, Card, ListGroup } from "react-bootstrap";
import "../static/employeeJob.css";

const Employee = () => {
  const { employeeId } = useParams();
  const user = useContext(MyUserContext);
  const [employeeJobs, setEmployeeJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadEmployeeJobs = async () => {
      try {
        setLoading(true);
        // Gọi API để lấy danh sách EmployeeJob theo employeeId
        const res = await authApis().get(`${endpoints["employeeJob/employee"]}${employeeId}`);
        console.log("Dữ liệu từ API:", res.data);
        setEmployeeJobs(res.data);
      } catch (err) {
        console.error("Lỗi khi tải dữ liệu:", err);
        if (err.response) {
          console.error("Chi tiết lỗi từ server:", err.response.data);
          setError(`Lỗi: ${err.response.status} - ${err.response.data.message || "Không thể tải dữ liệu"}`);
        } else {
          setError("Không thể kết nối đến server. Vui lòng thử lại!");
        }
      } finally {
        setLoading(false);
      }
    };

    loadEmployeeJobs();
  }, [employeeId]);

  // Hàm chuyển đổi jobState thành trạng thái hiển thị
  const getJobStateText = (jobState) => {
    if (jobState === 0) return "Bị từ chối ❌";
    if (jobState === 1) return "Đã được nhận ✅";
    return "Đang chờ phê duyệt ⌛";
  };

  // Hàm lấy class CSS cho trạng thái
  const getJobStateClass = (jobState) => {
    if (jobState === 0) return "employee-job-status rejected";
    if (jobState === 1) return "employee-job-status approved";
    return "employee-job-status pending";
  };

  if (loading) {
    return (
      <Container className="employee-job-container">
        <div className="employee-job-loading">Đang tải...</div>
      </Container>
    );
  }

  if (error) {
    return (
      <Container className="employee-job-container">
        <div className="employee-job-error">{error}</div>
      </Container>
    );
  }

  return (
    <Container className="employee-job-container">
      <h1 className="employee-job-title">Công Việc Ứng Tuyển Của Bạn</h1>
      {employeeJobs.length === 0 ? (
        <p className="employee-job-empty">Bạn chưa ứng tuyển vào công việc nào.</p>
      ) : (
        employeeJobs.map((employeeJob) => (
          <Card key={employeeJob.id} className="employee-job-card">
            <Card.Body className="employee-job-card-body">
              <Card.Title className="employee-job-card-title">
                {employeeJob.jobId?.name || "Tên công việc chưa xác định"}
              </Card.Title>
              <ListGroup variant="flush" className="employee-job-list-group">
                <ListGroup.Item className="employee-job-list-item">
                  <strong>Lương:</strong>
                  <span className="employee-job-salary">
                    {employeeJob.jobId?.salary ? `${employeeJob.jobId.salary} $` : "Thỏa thuận"}
                  </span>
                </ListGroup.Item>
                <ListGroup.Item className="employee-job-list-item">
                  <strong>Thời gian làm việc:</strong>
                  <span className="employee-job-time">
                    {employeeJob.jobId?.timeStart && employeeJob.jobId?.timeEnd
                      ? `${employeeJob.jobId.timeStart} - ${employeeJob.jobId.timeEnd}`
                      : "Chưa xác định"}
                  </span>
                </ListGroup.Item>
                <ListGroup.Item className="employee-job-list-item">
                  <strong>Địa chỉ:</strong>
                  <span className="employee-job-address">
                    {employeeJob.jobId?.employerId?.company?.address || "Chưa xác định"}
                  </span>
                </ListGroup.Item>
                <ListGroup.Item className="employee-job-list-item">
                  <strong>Mã số thuế:</strong>
                  <span className="employee-job-tax">
                    {employeeJob.jobId?.employerId?.company?.taxCode || "Chưa xác định"}
                  </span>
                </ListGroup.Item>
                <ListGroup.Item className="employee-job-list-item">
                  <strong>Trạng thái việc làm:</strong>
                  <span className={getJobStateClass(employeeJob.jobState)}>
                    {getJobStateText(employeeJob.jobState)}
                  </span>
                </ListGroup.Item>
              </ListGroup>
            </Card.Body>
          </Card>
        ))
      )}
    </Container>
  );
};

export default Employee;
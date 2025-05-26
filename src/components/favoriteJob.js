import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { authApis, endpoints } from "../configs/Api";
import { Container, Card, ListGroup, Button } from "react-bootstrap";
import "../static/favoriteJob.css";

const FavoriteJob = () => {
  const { employeeId } = useParams(); // Lấy employeeId từ URL
  const [favoriteJobs, setFavoriteJobs] = useState([]); // Sử dụng mảng để lưu danh sách công việc
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const loadFavoriteJobs = async () => {
      if (!employeeId) {
        setError("Không tìm thấy ID nhân viên.");
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        // Gọi API /employeeJob/employee/{employeeId}
        const res = await authApis().get(`${endpoints["employeeJob/employee"]}${employeeId}`);
        const jobs = res.data;

        // Lọc các công việc thỏa mãn điều kiện: favoriteJob = 1 và state = "approved"
        const filteredJobs = jobs.filter(
          (job) => job.favoriteJob === 1 && job.jobId.state === "approved"
        );

        setFavoriteJobs(filteredJobs);
        if (filteredJobs.length === 0) {
          setError("Không có công việc yêu thích nào thỏa mãn điều kiện.");
        }
      } catch (err) {
        console.error("Lỗi khi tải công việc yêu thích:", err);
        setError("Có lỗi xảy ra khi tải dữ liệu. Vui lòng thử lại sau.");
      } finally {
        setLoading(false);
      }
    };

    loadFavoriteJobs();
  }, [employeeId]);

  if (loading) return (
    <div className="favorite-job-container">
      <div className="favorite-job-loading">Đang tải...</div>
    </div>
  );
  
  if (error) return (
    <div className="favorite-job-container">
      <div className="favorite-job-error">{error}</div>
    </div>
  );

  return (
    <div className="favorite-job-container">
      <Container>
        <h2 className="favorite-job-title">Danh Sách Công Việc Yêu Thích</h2>
        {favoriteJobs.length === 0 ? (
          <div className="favorite-job-empty">Không có công việc yêu thích nào.</div>
        ) : (
          favoriteJobs.map((job) => (
            <Card key={job.id} className="favorite-job-card">
              <div className="favorite-job-card-body">
                <Card.Title className="favorite-job-card-title">
                  {job.jobId.name}
                </Card.Title>
                <ListGroup variant="flush" className="favorite-job-list">
                  <ListGroup.Item className="favorite-job-list-item favorite-job-icon-salary">
                    <strong>Lương:</strong> 
                    <span className="favorite-job-salary value">{job.jobId.salary} $</span>
                  </ListGroup.Item>
                  <ListGroup.Item className="favorite-job-list-item favorite-job-icon-time">
                    <strong>Thời gian làm việc:</strong> 
                    <span className="value">{job.jobId.timeStart} - {job.jobId.timeEnd}</span>
                  </ListGroup.Item>
                  <ListGroup.Item className="favorite-job-list-item favorite-job-icon-date">
                    <strong>Ngày tạo:</strong>
                    <span className="value">
                      {new Date(job.jobId.createdAt).toLocaleDateString("vi-VN")}
                    </span>
                  </ListGroup.Item>
                  <ListGroup.Item className="favorite-job-list-item favorite-job-icon-date">
                    <strong>Ngày cập nhật:</strong>
                    <span className="value">
                      {new Date(job.jobId.updatedAt).toLocaleDateString("vi-VN")}
                    </span>
                  </ListGroup.Item>
                  <ListGroup.Item className="favorite-job-list-item favorite-job-icon-category">
                    <strong>Danh mục:</strong> 
                    <span className="favorite-job-category value">
                      {job.jobId.categoryId?.name || "Chưa xác định"}
                    </span>
                  </ListGroup.Item>
                  <ListGroup.Item className="favorite-job-list-item favorite-job-icon-company">
                    <strong>Công ty:</strong>
                    <span className="favorite-job-company value">
                      {job.jobId.employerId?.company?.name || "Chưa xác định"}
                    </span>
                  </ListGroup.Item>
                  <ListGroup.Item className="favorite-job-list-item favorite-job-icon-address">
                    <strong>Địa chỉ:</strong>
                    <span className="value">
                      {job.jobId.employerId?.company?.address || "Chưa xác định"}
                    </span>
                  </ListGroup.Item>
                </ListGroup>
              </div>
            </Card>
          ))
        )}
        <div className="text-end">
          <Button 
            className="favorite-job-back-btn" 
            onClick={() => navigate(-1)}
          >
            ← Quay lại
          </Button>
        </div>
      </Container>
    </div>
  );
};

export default FavoriteJob;
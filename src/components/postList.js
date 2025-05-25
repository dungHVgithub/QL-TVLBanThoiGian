import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Card, Button } from 'react-bootstrap';
import { authApis, endpoints } from "../configs/Api";
import { FaDollarSign, FaTag, FaClock, FaFolder, FaBuilding, FaCalendarAlt, FaEdit, FaTrash } from 'react-icons/fa';
import '../static/jobList.css';
import { toast } from 'react-toastify';

const PostList = () => {
  const { employerId } = useParams();
  const navigate = useNavigate();
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadJobs = async () => {
      if (!employerId) {
        setError("Không tìm thấy employerId trong URL.");
        setLoading(false);
        return;
      }

      try {
        const res = await authApis().get(`${endpoints['job_postings']}/employer/${employerId}`);
        setJobs(res.data);
        setLoading(false);
      } catch (err) {
        console.error("Error loading jobs:", err);
        setError("Không thể tải danh sách bài đăng. Vui lòng thử lại sau.");
        setLoading(false);
      }
    };

    loadJobs();
  }, [employerId]);

  const handleUpdate = (jobId) => {
    // Điều hướng đến trang cập nhật với jobId
    navigate(`/updateJob/${jobId}`);
  };

  const handleDelete = async (jobId) => {
    // Hiển thị thông báo xác nhận
    if (window.confirm("Bạn có chắc muốn xóa bài đăng này?")) {
      try {
        await authApis().delete(`${endpoints['job_postings']}/${jobId}`);
        // Cập nhật state để loại bỏ job đã xóa
        setJobs(jobs.filter(job => job.id !== jobId));
        toast.success("Xóa bài đăng thành công!");
      } catch (err) {
        console.error("Error deleting job:", err);
        toast.error("Có lỗi xảy ra khi xóa bài đăng. Vui lòng thử lại sau.");
      }
    }
  };

  if (loading) {
    return <div className="container mt-4">Đang tải...</div>;
  }

  if (error) {
    return <div className="container mt-4 text-danger">{error}</div>;
  }

  return (
    <div className="job-list-container">
      <h1 className="mb-4">Danh sách bài đăng của bạn</h1>
      {jobs.length > 0 ? (
        jobs.map((job) => (
          <Card key={job.id} className="job-card">
            <Card.Body>
              <Card.Title className="job-card-title">{job.name}</Card.Title>
              <div className="card-content">
                <div className="job-info">
                  <div className="job-info-row">
                    <div className="job-info-col">
                      <FaDollarSign className="icon" />
                      <span className="value">Lương: {job.salary} USD</span>
                    </div>
                    <div className="job-info-col">
                      <FaTag className="icon" />
                      <span className="value">Trạng thái: {job.state}</span>
                    </div>
                  </div>
                  <div className="job-info-row">
                    <div className="job-info-col">
                      <FaClock className="icon" />
                      <span className="value">Thời gian bắt đầu: {job.timeStart}</span>
                    </div>
                    <div className="job-info-col">
                      <FaClock className="icon" />
                      <span className="value">Thời gian kết thúc: {job.timeEnd}</span>
                    </div>
                  </div>
                  <div className="job-info-row">
                    <div className="job-info-col">
                      <FaFolder className="icon" />
                      <span className="value">Danh mục: {job.categoryId.name}</span>
                    </div>
                    <div className="job-info-col">
                      <FaBuilding className="icon" />
                      <span className="value">Công ty: {job.employerId.company.name}</span>
                    </div>
                  </div>
                  <div className="job-info-row">
                    <div className="job-info-col">
                      <FaCalendarAlt className="icon" />
                      <span className="value">Ngày tạo: {new Date(job.createdAt).toLocaleDateString()}</span>
                    </div>
                    <div className="job-info-col">
                      <FaCalendarAlt className="icon" />
                      <span className="value">Ngày cập nhật: {new Date(job.updatedAt).toLocaleDateString()}</span>
                    </div>
                  </div>
                </div>
                <div className="action-buttons">
                  <Button
                    className="btn-update"
                    onClick={() => handleUpdate(job.id)}
                  >
                    <FaEdit /> Cập nhật
                  </Button>
                  <Button
                    className="btn-delete"
                    onClick={() => handleDelete(job.id)}
                  >
                    <FaTrash /> Xóa
                  </Button>
                </div>
              </div>
            </Card.Body>
          </Card>
        ))
      ) : (
        <p className="no-jobs">Không có bài đăng nào.</p>
      )}
    </div>
  );
};

export default PostList;
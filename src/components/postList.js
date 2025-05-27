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
    return <div className="post-list-loading">Đang tải...</div>;
  }

  if (error) {
    return <div className="post-list-error">{error}</div>;
  }

  return (
    <div className="post-list-container">
      <h1>Danh sách bài đăng của bạn</h1>
      {jobs.length > 0 ? (
        jobs.map((job) => (
          <Card key={job.id} className="job-posting-card">
            <Card.Body>
              <Card.Title className="job-posting-title">{job.name}</Card.Title>
              <div className="job-posting-content">
                <div className="job-posting-info">
                  <div className="job-info-section">
                    <div className="job-info-item">
                      <FaDollarSign className="job-info-icon" />
                      <div className="job-info-content">
                        <strong>Lương:</strong> {job.salary} USD
                      </div>
                    </div>
                    <div className="job-info-item">
                      <FaTag className="job-info-icon" />
                      <div className="job-info-content">
                        <strong>Trạng thái:</strong> {job.state}
                      </div>
                    </div>
                  </div>
                  
                  <div className="job-info-section">
                    <div className="job-info-item">
                      <FaClock className="job-info-icon" />
                      <div className="job-info-content">
                        <strong>Thời gian bắt đầu:</strong> {job.timeStart}
                      </div>
                    </div>
                    <div className="job-info-item">
                      <FaClock className="job-info-icon" />
                      <div className="job-info-content">
                        <strong>Thời gian kết thúc:</strong> {job.timeEnd}
                      </div>
                    </div>
                  </div>
                  
                  <div className="job-info-section">
                    <div className="job-info-item">
                      <FaFolder className="job-info-icon" />
                      <div className="job-info-content">
                        <strong>Danh mục:</strong> {job.categoryId.name}
                      </div>
                    </div>
                    <div className="job-info-item">
                      <FaBuilding className="job-info-icon" />
                      <div className="job-info-content">
                        <strong>Công ty:</strong> {job.employerId.company.name}
                      </div>
                    </div>
                  </div>
                  
                  <div className="job-info-section">
                    <div className="job-info-item">
                      <FaCalendarAlt className="job-info-icon" />
                      <div className="job-info-content">
                        <strong>Ngày tạo:</strong> {new Date(job.createdAt).toLocaleDateString()}
                      </div>
                    </div>
                    <div className="job-info-item">
                      <FaCalendarAlt className="job-info-icon" />
                      <div className="job-info-content">
                        <strong>Ngày cập nhật:</strong> {new Date(job.updatedAt).toLocaleDateString()}
                      </div>
                    </div>
                  </div>
                </div>
                
                <div className="job-action-buttons">
                  <Button
                    className="job-btn-update"
                    onClick={() => handleUpdate(job.id)}
                  >
                    <FaEdit /> Cập nhật
                  </Button>
                  <Button
                    className="job-btn-delete"
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
        <div className="post-list-no-jobs">
          <h2>Chưa có bài đăng nào</h2>
          <p>Bạn chưa tạo bài đăng việc làm nào. Hãy tạo bài đăng đầu tiên của bạn!</p>
        </div>
      )}
    </div>
  );
};

export default PostList;
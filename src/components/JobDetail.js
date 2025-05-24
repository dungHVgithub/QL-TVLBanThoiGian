import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import Api, { endpoints } from "../configs/Api";
import ou from "../img/ou.png"; // Hình ảnh mặc định
import '../static/job_detail.css';
import { FaMapMarkerAlt, FaCalendarAlt, FaDollarSign, FaUser, FaClock, FaCheckCircle, FaFileAlt } from "react-icons/fa";

const JobDetail = () => {
  const { id } = useParams();
  const [jobDetail, setJobDetail] = useState(null);
  const [companyImages, setCompanyImages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const loadJobDetail = async () => {
    try {
      setLoading(true);
      const res = await Api.get(`${endpoints['job_details_by_job_posting']}/${id}`);
      setJobDetail(res.data);
    } catch (err) {
      console.error("Lỗi khi lấy chi tiết công việc:", err);
      setError("Không thể tải thông tin công việc. Vui lòng thử lại sau.");
    } finally {
      setLoading(false);
    }
  };

  const loadCompanyImages = async (companyId) => {
    if (!companyId) return;
    try {
      const res = await Api.get(`${endpoints['company_images']}/${companyId}`);
      const data = Array.isArray(res.data) ? res.data : [res.data];
      setCompanyImages(data);
    } catch (err) {
      console.error("Lỗi khi lấy hình ảnh công ty:", err);
      setCompanyImages([]);
    }
  };

  const formatDate = (timestamp) => {
    if (!timestamp) return "Chưa xác định";
    const date = new Date(timestamp);
    if (isNaN(date.getTime())) return "Chưa xác định";
    return `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()}`; // Định dạng DD/MM/YYYY
  };

  // Hàm xử lý khi nhấn nút Ứng tuyển
  const handleApply = () => {
    console.log("Người dùng đã nhấn nút Ứng tuyển cho công việc ID:", id);
    // Thêm logic ứng tuyển tại đây (gọi API, mở modal, v.v.)
  };

  // Hàm xử lý khi nhấn nút Thêm yêu thích
  const handleFavorite = () => {
    console.log("Người dùng đã thêm công việc ID:", id, "vào danh sách yêu thích");
    // Thêm logic thêm vào danh sách yêu thích tại đây (gọi API, cập nhật trạng thái, v.v.)
  };

  useEffect(() => {
    loadJobDetail();
  }, [id]);

  useEffect(() => {
    if (jobDetail && jobDetail.jobPosting?.employerId?.company?.id) {
      loadCompanyImages(jobDetail.jobPosting.employerId.company.id);
    }
  }, [jobDetail]);

  if (loading) return <div className="job-detail-loading">Đang tải...</div>;
  if (error) return <div className="job-detail-error">{error}</div>;
  if (!jobDetail) return <div className="job-detail-error">Không tìm thấy công việc.</div>;

  return (
    <div className="job-detail-container">
      <h2 className="job-detail-title">Chi tiết công việc</h2>
      <div className="job-detail-wrapper">
        {/* Phần thông tin công việc (70%) */}
        <div className="job-detail-info">
          <div className="job-section">
            <h3 className="job-section-title">Thông tin công việc</h3>
            <div className="job-info-row">
              <p><FaMapMarkerAlt className="icon" /> Địa điểm: {jobDetail.jobPosting?.employerId?.company?.address || "Chưa xác định"}</p>
              <p>
                <FaCalendarAlt className="icon" /> 
                Ngày đăng: {formatDate(jobDetail.jobPosting?.createdAt)} - 
                Ngày cập nhật: {formatDate(jobDetail.jobPosting?.updatedAt)}
              </p>
            </div>
            <div className="job-info-row">
              <p><FaDollarSign className="icon" /> Lương: {jobDetail.jobPosting?.salary ? `${jobDetail.jobPosting.salary} $` : "Thỏa thuận"}</p>
              <p><FaUser className="icon" /> Kinh nghiệm: {jobDetail.experience ? `${jobDetail.experience} năm` : "Chưa xác định"} - Cấp độ: {jobDetail.level || "Chưa xác định"}</p>
            </div>
            <div className="job-info-row">
              <p><FaClock className="icon" /> Hạn nộp: {formatDate(jobDetail.submitEnd)}</p>
              <p><FaCheckCircle className="icon" /> Phúc lợi: {jobDetail.benefit || "Chưa xác định"}</p>
            </div>
          </div>
          <div className="job-section">
            <h3 className="job-section-title">Mô tả công việc</h3>
            <p><FaFileAlt className="icon" /> {jobDetail.description || "Chưa có mô tả"}</p>
          </div>
        </div>

        {/* Phần hình ảnh công ty (30%) */}
        <div className="job-company-section">
          <div className="company-images">
            <h3 className="company-images-title">Hình ảnh công ty</h3>
            {companyImages.length > 0 ? (
              <div className="images-list">
                {companyImages.map((image) => (
                  <div key={image.id} className="image-item">
                    <img
                      src={image.imagePath || ou}
                      alt={image.caption || "Hình ảnh công ty"}
                      className="company-image"
                      onError={(e) => (e.target.src = ou)}
                    />
                    <p className="image-caption">{image.caption || "Không có chú thích"}</p>
                  </div>
                ))}
              </div>
            ) : (
              <p className="no-images">Không có hình ảnh công ty.</p>
            )}
          </div>
        </div>
      </div>
      <div className="job-actions">
        <button className="apply-button" onClick={handleApply}>Ứng tuyển</button>
        <button className="favorite-button" onClick={handleFavorite}>Thêm yêu thích</button>
      </div>
    </div>
  );
};

export default JobDetail;
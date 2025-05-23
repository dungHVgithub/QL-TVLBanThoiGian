import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import Api, { endpoints } from "../configs/Api";
import ou from "../img/ou.png"; // Hình ảnh mặc định
import '../static/job_detail.css';
import { IconName } from "react-icons/fc";


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
    return isNaN(date.getTime()) ? "Chưa xác định" : date.toLocaleDateString();
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
        {/* Phần thông tin công việc (60%) */}
        <div className="job-detail-info">
          <div className="job-section">
            <h3 className="job-section-title">Thông tin công việc</h3>
            <p><span className="icon-location"></span> Địa điểm: {jobDetail.jobPosting?.employerId?.company?.address || "Chưa xác định"}</p>
            <p><span className="icon-calendar"></span> Ngày đăng: {formatDate(jobDetail.jobPosting?.createdAt)}</p>
            <p><span className="icon-money"></span> Lương: {jobDetail.jobPosting?.salary ? `${jobDetail.jobPosting.salary} $` : "Thỏa thuận"}</p>
            <p><span className="icon-user"></span> Kinh nghiệm: {jobDetail.experience || "Chưa xác định"}</p>
            <p><span className="icon-clock"></span> Hạn nộp: {formatDate(jobDetail.submitEnd)}</p>
            <p><span className="icon-check"></span> Phúc lợi: {jobDetail.benefit || "Chưa xác định"}</p>
          </div>
          <div className="job-section">
            <h3 className="job-section-title">Mô tả công việc</h3>
            <p><span className="icon-description"></span> {jobDetail.description || "Chưa có mô tả"}</p>
          </div>
        </div>

        {/* Phần hình ảnh công ty (40%) */}
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
    </div>
  );
};

export default JobDetail;
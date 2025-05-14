import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import Api, { endpoints } from "../configs/Api";
import ou from "../img/ou.png";
import "../static/home.css";

const Home = () => {
  const [jobPostings, setJobPostings] = useState([]);
  const [companyImages, setCompanyImages] = useState({}); // State lưu trữ hình ảnh theo companyId
  const [page, setPage] = useState(1);
  const [loading, setLoading] = useState(false);
  const [q] = useSearchParams();

  // Hàm lấy danh sách công việc
  const loadJob = async () => {
    if (page > 0) {
      try {
        setLoading(true);
        let url = `${endpoints["job_postings"]}?page=${page}`;
        let cateId = q.get("categoryId");
        if (cateId) {
          url = `${url}&categoryId=${cateId}`;
        }
        let kw = q.get("kw");
        if (kw) {
          url = `${url}&kw=${kw}`;
        }
        let res = await Api.get(url);
        console.log("Job postings response:", res.data); // Debug response
        if (res.data.length === 0) {
          setPage(0);
        } else {
          if (page === 1) {
            setJobPostings(res.data);
          } else {
            setJobPostings([...jobPostings, ...res.data]);
          }
        }
      } catch (error) {
        console.error("Lỗi khi lấy danh sách công việc:", error);
      } finally {
        setLoading(false);
      }
    }
  };

  // Hàm lấy hình ảnh công ty dựa trên companyIds
  const loadCompanyImage = async (jobs) => {
    if (!jobs || jobs.length === 0) return; // Ngăn gọi nếu jobs không hợp lệ
    try {
      // Lấy danh sách companyId duy nhất từ jobs
      const companyIds = [...new Set(jobs.map(job => job.employerId?.company?.id).filter(id => id))];
      console.log("Company IDs:", companyIds); // Debug companyIds

      // Lấy hình ảnh cho từng companyId
      const images = { ...companyImages };
      for (const companyId of companyIds) {
        if (!images[companyId]) {
          try {
            const imageRes = await Api.get(`${endpoints["company_images"]}/${companyId}`);
            console.log(`Image response for company ${companyId}:`, imageRes.data); // Debug response
            if (imageRes.data && imageRes.data.length > 0) {
              // Sắp xếp theo uploadTime giảm dần (mới nhất trước) và lọc bỏ imagePath rỗng
              const sortedImages = [...imageRes.data]
                .filter(img => img.imagePath && img.imagePath.trim() !== "")
                .sort((a, b) => b.uploadTime - a.uploadTime);
              images[companyId] = sortedImages.length > 0 ? sortedImages[0].imagePath : ou;
              console.log(`Assigned latest imagePath for ${companyId}:`, images[companyId]);
            } else {
              images[companyId] = ou;
              console.log(`No image found for company ${companyId}, using default:`, ou);
            }
          } catch (err) {
            console.error(`Lỗi khi lấy hình ảnh công ty ${companyId}:`, err);
            images[companyId] = ou; // Dùng ảnh mặc định nếu lỗi
          }
        }
      }
      setCompanyImages(images);
    } catch (error) {
      console.error("Lỗi khi lấy hình ảnh công ty:", error);
    }
  };

  // Gọi loadJob và loadCompanyImage khi component mount hoặc khi query thay đổi
  useEffect(() => {
    loadJob();
  }, [page, q]);

  // Gọi loadCompanyImage khi jobPostings thay đổi
  useEffect(() => {
    loadCompanyImage(jobPostings);
  }, [jobPostings]);

  // Reset page và jobPostings khi query thay đổi
  useEffect(() => {
    setPage(1);
    setJobPostings([]);
    setCompanyImages({}); // Reset hình ảnh khi query thay đổi
  }, [q]);

  const loadMore = () => {
    if (!loading && page > 0) {
      setPage(page + 1);
    }
  };

  // Lọc các job có trạng thái "approved"
  const approvedJobs = jobPostings.filter((job) => job.state === "approved") || [];

  // Hàm format thời gian HH:mm:ss thành HH:mm
  const formatTime = (time) => {
    if (!time) return "Chưa xác định";
    return time.split(":").slice(0, 2).join(":");
  };

  // Hàm format ngày từ timestamp
  const formatDate = (timestamp) => {
    if (!timestamp) return "Chưa xác định";
    const date = new Date(timestamp);
    return isNaN(date.getTime()) ? "Chưa xác định" : date.toLocaleDateString();
  };

  // Hàm lấy thông tin công ty trực tiếp từ employerId.company
  const getCompanyInfo = (employer) => {
    if (!employer || !employer.company) {
      return { name: "Chưa xác định", address: "Chưa xác định", imagePath: ou };
    }
    const imagePath = companyImages[employer.company.id] || ou;
    return {
      name: employer.company.name || "Chưa xác định",
      address: employer.company.address || "Chưa xác định",
      imagePath,
    };
  };

  return (
    <>
      <div className="home-container">
        {approvedJobs.length > 0 ? (
          approvedJobs.map((job) => {
            const companyInfo = getCompanyInfo(job.employerId); // Lấy thông tin công ty
            return (
              <div key={job.id} className="job-card">
                <img
                  src={companyInfo.imagePath}
                  alt={companyInfo.name || "Company Logo"}
                  className="job-logo"
                  onError={(e) => {
                    console.error("Image load error for URL:", companyInfo.imagePath, e);
                    e.target.src = ou;
                  }}
                />
                <div className="job-content">
                  <h3 className="job-title">{job.description || "Mô tả công việc"}</h3>
                  <p className="job-details">
                    💰 {job.salary ? `Lương: ${job.salary} $` : "Lương: Thỏa thuận"} - 
                    📅 {job.submitEnd ? `Hạn nộp: ${formatDate(job.submitEnd)}` : "Hạn nộp: Chưa xác định"}
                  </p>
                  <p className="job-time">
                    🕒 Bắt đầu: {formatTime(job.timeStart)} - Kết thúc: {formatTime(job.timeEnd)}
                  </p>
                  <p className="company-info">
                    🏢 Công ty: {companyInfo.name} - 📍 Địa chỉ: {companyInfo.address}
                  </p>
                </div>
                <div className="action-buttons">
                  <button className="apply-btn">Ứng tuyển</button>
                  <span className="heart-icon">♡</span>
                </div>
              </div>
            );
          })
        ) : (
          <p>Không có công việc nào được phê duyệt.</p>
        )}
        {page > 0 && (
          <button onClick={loadMore} disabled={loading} className="load-more-btn">
            {loading ? "Đang tải..." : "Tải thêm"}
          </button>
        )}
      </div>
    </>
  );
};

export default Home;
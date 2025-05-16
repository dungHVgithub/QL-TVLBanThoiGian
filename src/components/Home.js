import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import Api, { endpoints } from "../configs/Api";
import ou from "../img/ou.png";
import "../static/home.css";

const Home = () => {
  const [jobPostings, setJobPostings] = useState([]);
  const [companyImages, setCompanyImages] = useState({});
  const [companyInfos, setCompanyInfos] = useState({});
  const [page, setPage] = useState(1);
  const [loading, setLoading] = useState(false);
  const [q] = useSearchParams();

  // Load danh sách công việc
  const loadJob = async () => {
    if (page > 0) {
      try {
        setLoading(true);
        let url = `${endpoints["job_postings"]}?page=${page}`;
        let cateId = q.get("categoryId");
        if (cateId) url += `&categoryId=${cateId}`;
        let kw = q.get("kw");
        if (kw) url += `&kw=${kw}`;
        const res = await Api.get(url);
        if (res.data.length === 0) {
          setPage(0);
        } else {
          if (page === 1) {
            setJobPostings(res.data);
          } else {
            setJobPostings(prev => [...prev, ...res.data]);
          }
        }
      } catch (error) {
        console.error("Lỗi khi lấy danh sách công việc:", error);
      } finally {
        setLoading(false);
      }
    }
  };

  // Load logo công ty (ảnh có caption bắt đầu bằng "Logo")
  const loadCompanyImage = async (jobs) => {
    if (!jobs || jobs.length === 0) return;
    try {
      const companyIds = [...new Set(jobs.map(job => job.employerId?.company).filter(id => id))];
      const images = { ...companyImages };

      for (const companyId of companyIds) {
        if (!images[companyId]) {
          try {
            const res = await Api.get(`${endpoints["company_images"]}/${companyId}`);
            const logo = res.data
              .filter(img => img.caption?.toLowerCase().startsWith("logo"))
              .sort((a, b) => b.uploadTime - a.uploadTime)[0];
            images[companyId] = logo?.imagePath || ou;
          } catch (err) {
            console.error(`Lỗi khi lấy ảnh công ty ${companyId}:`, err);
            images[companyId] = ou;
          }
        }
      }

      setCompanyImages(images);
    } catch (error) {
      console.error("Lỗi khi lấy hình ảnh công ty:", error);
    }
  };

  // Load thông tin công ty từ API company_info
  const loadCompanyInfos = async (jobs) => {
    if (!jobs || jobs.length === 0) return;
    try {
      const companyIds = [...new Set(jobs.map(job => job.employerId?.company).filter(id => id))];
      const infos = { ...companyInfos };

      const res = await Api.get(endpoints["company_info"]);
      for (const id of companyIds) {
        if (!infos[id]) {
          const found = res.data.find(c => c.id === id);
          if (found) {
            infos[id] = {
              name: found.name,
              address: found.address,
            };
          } else {
            infos[id] = {
              name: "Chưa xác định",
              address: "Chưa xác định",
            };
          }
        }
      }

      setCompanyInfos(infos);
    } catch (error) {
      console.error("Lỗi khi load thông tin công ty:", error);
    }
  };

  // Lấy thông tin công ty từ state
  const getCompanyInfo = (employer) => {
    const companyId = employer?.company;
    if (!companyId) {
      return { name: "Chưa xác định", address: "Chưa xác định", imagePath: ou };
    }

    return {
      name: companyInfos[companyId]?.name || "Chưa xác định",
      address: companyInfos[companyId]?.address || "Chưa xác định",
      imagePath: companyImages[companyId] || ou,
    };
  };

  // Hàm định dạng thời gian
  const formatTime = (time) => {
    if (!time) return "Chưa xác định";
    return time.split(":").slice(0, 2).join(":");
  };

  // Hàm định dạng ngày tháng từ timestamp
  const formatDate = (timestamp) => {
    if (!timestamp) return "Chưa xác định";
    const date = new Date(timestamp);
    return isNaN(date.getTime()) ? "Chưa xác định" : date.toLocaleDateString();
  };

  // Khi component mount hoặc page/query thay đổi
  useEffect(() => {
    loadJob();
  }, [page, q]);

  // Khi jobPostings thay đổi, gọi load dữ liệu phụ trợ
  useEffect(() => {
    loadCompanyInfos(jobPostings);
    loadCompanyImage(jobPostings);
  }, [jobPostings]);

  // Reset khi query thay đổi
  useEffect(() => {
    setPage(1);
    setJobPostings([]);
    setCompanyImages({});
    setCompanyInfos({});
  }, [q]);

  const loadMore = () => {
    if (!loading && page > 0) {
      setPage(page + 1);
    }
  };

  const approvedJobs = jobPostings.filter((job) => job.state === "approved") || [];

  return (
    <div className="home-container">
      {approvedJobs.length > 0 ? (
        approvedJobs.map((job) => {
          const companyInfo = getCompanyInfo(job.employerId);
          return (
            <div key={job.id} className="job-card">
              <img
                src={companyInfo.imagePath}
                alt={companyInfo.name}
                className="job-logo"
                onError={(e) => {
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
  );
};

export default Home;

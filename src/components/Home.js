import { useEffect, useState } from "react";
import { useLocation, useNavigate, useSearchParams } from "react-router-dom";
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
  const [kw, setKw] = useState();
  const nav = useNavigate();
  const locationHome = useLocation();
  const [jobLocation, setJobLocation] = useState();
  const [salary, setSalary] = useState();
  const [companyName, setCompanyName] = useState();

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

        // 👇 Lọc thêm tại frontend theo địa điểm nếu có
        let location = q.get("location");
        let filteredJobs = res.data;
        if (location) {
          const lowerLocation = location.toLowerCase();
          filteredJobs = res.data.filter(job => {
            const companyId = job.employerId?.company;
            const info = companyInfos[companyId];
            return info?.address?.toLowerCase().includes(lowerLocation);
          });
        }

        // Lọc theo lương
        let minSalary = q.get("salary");
        if (minSalary) {
          filteredJobs = filteredJobs.filter(job =>
            job.salary && parseFloat(job.salary) >= parseFloat(minSalary)
          );
        }

        let companyName = q.get("companyName");
        if (companyName) {
          const lowerCompany = companyName.toLowerCase();
          filteredJobs = filteredJobs.filter(job => {
            const companyId = job.employerId?.company;
            const info = companyInfos[companyId];
            return info?.name?.toLowerCase().includes(lowerCompany);
          });
        }

        if (filteredJobs.length === 0) {
          setPage(0);
        } else {
          if (page === 1) {
            setJobPostings(filteredJobs);
          } else {
            setJobPostings(prev => [...prev, ...filteredJobs]);
          }
        }
      } catch (error) {
        console.error("Lỗi khi lấy danh sách công việc:", error);
      } finally {
        setLoading(false);
      }
    }
  };

  // Load logo công ty
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

  // Load thông tin công ty
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

  const formatTime = (time) => {
    if (!time) return "Chưa xác định";
    return time.split(":").slice(0, 2).join(":");
  };

  const formatDate = (timestamp) => {
    if (!timestamp) return "Chưa xác định";
    const date = new Date(timestamp);
    return isNaN(date.getTime()) ? "Chưa xác định" : date.toLocaleDateString();
  };

  useEffect(() => {
    loadJob();
  }, [page, q]);

  

  useEffect(() => {
    loadCompanyInfos(jobPostings);
    loadCompanyImage(jobPostings);
  }, [jobPostings]);

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

  const search = (e) => {
    e.preventDefault();
    const params = new URLSearchParams();
    if (kw) params.append("kw", kw);
    if (jobLocation) params.append("location", jobLocation);
    if (salary) params.append("salary", salary);
    if (companyName) params.append("companyName", companyName);
    nav("/?" + params.toString());
  };

  const approvedJobs = jobPostings.filter((job) => job.state === "approved") || [];

  const clearFilters = () => {
  setKw("");
  setCompanyName("");
  setJobLocation("");
  setSalary("");
  nav("/", { replace: true }); 
};


  return (
    <div className="home-container">
      <div className="filter-section">
        <form onSubmit={search} className="filter-form">
          <div className="filter-group">
            <label>Tìm công việc</label>
            <input
              type="search"
              placeholder="Nhập tên công việc"
              value={kw}
              onChange={e => setKw(e.target.value)}
            />
          </div>
          <div className="filter-group">
            <label>Địa điểm</label>
            <input
              type="search"
              placeholder="Nhập địa điểm"
              value={jobLocation}
              onChange={(e) => setJobLocation(e.target.value)}
            />
          </div>
          <div className="filter-group">
            <label>Lương tối thiểu ($)</label>
            <input
              type="number"
              placeholder="VD: 1000"
              value={salary}
              onChange={(e) => setSalary(e.target.value)}
            />
          </div>
          <div className="filter-group">
            <label>Tên công ty</label>
            <input
              type="search"
              placeholder="VD: FPT"
              value={companyName}
              onChange={(e) => setCompanyName(e.target.value)}
            />
          </div>
          <button type="submit" className="filter-btn">Lọc</button>
          <button type="button" className="filter-btn reset-btn" onClick={clearFilters}>Xoá bộ lọc</button>
        </form>
      </div>
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
                  💰 {job.salary ? `Lương: ${job.salary} $` : "Lương: Thỏa thuận"} - 📅 {job.submitEnd ? `Hạn nộp: ${formatDate(job.submitEnd)}` : "Hạn nộp: Chưa xác định"}
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

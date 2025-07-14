import { useEffect, useState } from "react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import Api, { endpoints } from "../configs/Api";
import ou from "../img/ou.png";
import iconCate from "../img/iconCate.png";
import "../static/home.css";

const Home = () => {
  const [jobPostings, setJobPostings] = useState([]);
  const [companyImages, setCompanyImages] = useState({});
  const [page, setPage] = useState(1);
  const [loading, setLoading] = useState(false);
  const [q] = useSearchParams();
  const nav = useNavigate();

  const [filters, setFilters] = useState({
    kw: "",
    location: "",
    salary: "",
    companyName: "",
  });

  const loadJob = async () => {
    if (page <= 0) return;

    try {
      setLoading(true);
      let url = `${endpoints["job_postings"]}?page=${page}`;
      const cateId = q.get("categoryId");
      if (cateId) url += `&categoryId=${cateId}`;
      const kw = q.get("kw");
      if (kw) url += `&kw=${kw}`;

      const res = await Api.get(url);
      let filteredJobs = Array.isArray(res.data) ? res.data : [];

      const location = q.get("location");
      if (location) {
        const lowerLocation = location.toLowerCase();
        filteredJobs = filteredJobs.filter(job => {
          const address = job.employer?.company?.address;
          return address?.toLowerCase().includes(lowerLocation) || false;
        });
      }

      const minSalary = q.get("salary");
      if (minSalary) {
        filteredJobs = filteredJobs.filter(job =>
          job.salary && parseFloat(job.salary) >= parseFloat(minSalary)
        );
      }

      const companyName = q.get("companyName");
      if (companyName) {
        const lowerCompany = companyName.toLowerCase();
        filteredJobs = filteredJobs.filter(job => {
          const name = job.employer?.company?.name;
          return name?.toLowerCase().includes(lowerCompany) || false;
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
      setJobPostings([]);
    } finally {
      setLoading(false);
    }
  };

  const loadCompanyImage = async (jobs) => {
    if (!jobs || jobs.length === 0) return;
    let images = { ...companyImages };

    const companyIdsToFetch = [...new Set(
      jobs.map(job => job.employer?.company?.id)
        .filter(id => id && typeof id === 'number' && !images[id])
    )];

    if (companyIdsToFetch.length === 0) return;

    try {
      for (const companyId of companyIdsToFetch) {
        const res = await Api.get(`${endpoints["company_images"]}/${companyId}`);
        const data = Array.isArray(res.data) ? res.data : [res.data];
        const logo = data.find(image =>
          image.caption?.toLowerCase().includes("logo")
        ) || data.sort((a, b) => new Date(b.uploadTime) - new Date(a.uploadTime))[0];
        images[companyId] = logo?.imagePath || ou;
      }

      setCompanyImages(images);
    } catch (error) {
      console.error(`Error fetching company images:`, error.message, error.response?.data);
      companyIdsToFetch.forEach(id => {
        if (!images[id]) images[id] = ou;
      });
      setCompanyImages(images);
    }
  };

  const getCompanyInfo = (employer) => {
    const company = employer?.company || {};
    return {
      name: company?.name || "Chưa xác định",
      address: company?.address || "Chưa xác định",
      imagePath: companyImages[company?.id] || ou,
      id: company?.id
    };
  };

  const formatTime = (time) => {
    if (!time) return "Chưa xác định";
    return time.split(":").slice(0, 2).join(":");
  };

  useEffect(() => {
    loadJob();
  }, [page, q]);

  useEffect(() => {
    loadCompanyImage(jobPostings);
  }, [jobPostings]);

  useEffect(() => {
    setPage(1);
    setJobPostings([]);
    setCompanyImages({});
  }, [q]);

  const loadMore = () => {
    if (!loading && page > 0) {
      setPage(prev => prev + 1);
    }
  };

  const search = (e) => {
    e.preventDefault();
    const params = new URLSearchParams();

    Object.entries(filters).forEach(([key, value]) => {
      if (value?.trim()) params.append(key, value);
    });

    nav("/?" + params.toString());
  };

  const clearFilters = () => {
    setFilters({ kw: "", location: "", salary: "", companyName: "" });
    nav("/", { replace: true });
  };

  const approvedJobs = Array.isArray(jobPostings)
    ? jobPostings.filter((job) => job.state === "approved")
    : [];

  return (
    <div className="home-container">
      <div className="filter-section">
        <form onSubmit={search} className="filter-form">
          {["kw", "location", "salary", "companyName"].map((key) => (
            <div className="filter-group" key={key}>
              <label>{key}</label>
              <input
                type="text"
                placeholder={`Nhập ${key}`}
                value={filters[key]}
                onChange={(e) => setFilters({ ...filters, [key]: e.target.value })}
              />
            </div>
          ))}
          <button type="submit" className="filter-btn">Lọc</button>
          <button type="button" className="filter-btn reset-btn" onClick={clearFilters}>Xoá bộ lọc</button>
        </form>
      </div>

      {approvedJobs.length > 0 ? (
        approvedJobs.map((job) => {
          const companyInfo = getCompanyInfo(job.employer);

          return (
            <div
              key={job.id}
              className="job-card"
              onClick={() => nav(`/job_detail/${job.id}`)}
              style={{ cursor: "pointer" }}
            >
              <img
                src={companyInfo.imagePath}
                alt={companyInfo.name}
                className="job-logo"
                onError={(e) => {
                  e.target.src = ou;
                }}
              />
              <div className="job-content">
                <h3 className="job-title job-title-link">{job.name || "Tên công việc"}</h3>
                <p className="job-details">
                  💰 {job.salary ? `Lương: ${job.salary} $` : "Lương: Thỏa thuận"} -
                  <img
                    src={iconCate}
                    alt="Loại công việc"
                    style={{ width: "20px", height: "20px", margin: "7px", verticalAlign: "middle" }}
                  />
                  Loại công việc: {job.categoryId?.name || "Chưa xác định"}
                </p>
                <p className="job-time">
                  🕒 Bắt đầu: {formatTime(job.timeStart)} - Kết thúc: {formatTime(job.timeEnd)}
                </p>
                <p className="company-info">
                  🏢 Công ty:
                  <Link
                    to={`/company_info/${companyInfo.id}`}
                    onClick={(e) => e.stopPropagation()}
                    className="company-link"
                  >
                    {companyInfo.name}
                  </Link>
                  - 📍 Địa chỉ: {companyInfo.address}
                </p>
              </div>
              <div className="action-buttons" onClick={(e) => e.stopPropagation()}>
                <button className="apply-btn">Ứng tuyển</button>
                <span className="heart-icon">♡</span>
              </div>
            </div>
          );
        })
      ) : (
        <p>Đang tải dữ liệu công việc...</p>
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
import { useEffect, useState, useContext } from "react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import Api, { endpoints, authApis } from "../configs/Api";
import ou from "../img/ou.png";
import iconCate from "../img/iconCate.png";
import "../static/home.css";
import { MyUserContext, MyDispatchContext } from "../configs/MyContexts";
import cookie from "react-cookies";

const Home = () => {
  const [jobPostings, setJobPostings] = useState([]);
  const [companyImages, setCompanyImages] = useState({});
  const [page, setPage] = useState(1);
  const [loading, setLoading] = useState(false);
  const [q] = useSearchParams();
  const nav = useNavigate();
  const user = useContext(MyUserContext);
  const dispatch = useContext(MyDispatchContext);
  const [favoriteJobs, setFavoriteJobs] = useState([]);
  const [filters, setFilters] = useState({ kw: "", location: "", salary: "", companyName: "" });

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
        filteredJobs = filteredJobs.filter((job) => {
          const address = job.employer?.company?.address;
          return address?.toLowerCase().includes(lowerLocation) || false;
        });
      }

      const minSalary = q.get("salary");
      if (minSalary) {
        filteredJobs = filteredJobs.filter(
          (job) => job.salary && parseFloat(job.salary) >= parseFloat(minSalary)
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
          setJobPostings((prev) => [...prev, ...filteredJobs]);
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
    try {
      const companyIds = [
        ...new Set(
          jobs.map((job) => job.employer?.company?.id).filter((id) => id && typeof id === "number")
        ),
      ];
      const images = { ...companyImages };

      for (const companyId of companyIds) {
        if (!images[companyId]) {
          try {
            const res = await Api.get(`${endpoints["company_images"]}/${companyId}`);
            const data = Array.isArray(res.data) ? res.data : [res.data];
            const logo = data.find((image) => image.caption?.toLowerCase().includes("logo")) ||
              data.sort((a, b) => b.uploadTime - a.uploadTime)[0];
            images[companyId] = logo?.imagePath || ou;
          } catch (error) {
            console.error(`Lỗi khi lấy ảnh công ty ${companyId}:`, error.message, error.response?.data);
            images[companyId] = ou;
          }
        }
      }

      setCompanyImages(images);
    } catch (error) {
      console.error(`Error fetching company images:`, error.message, error.response?.data);
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

  const formatDate = (timestamp) => {
    if (!timestamp) return "Chưa xác định";
    const date = new Date(timestamp);
    return isNaN(date.getTime()) ? "Chưa xác định" : date.toLocaleDateString();
  };

  const loadFavoriteJobs = async (employeeId) => {
    try {
      const res = await authApis().get(`${endpoints["employeeJob/employee"]}${employeeId}`);
      const jobs = res.data;
      setFavoriteJobs(jobs.filter((job) => job.favoriteJob === 1));
    } catch (error) {
      console.error("Lỗi khi lấy danh sách công việc yêu thích:", error);
      setFavoriteJobs([]);
    }
  };

  const getEmployeeId = async () => {
    if (!user || !user.id) return null;
    try {
      const res = await authApis().get(endpoints.employees);
      const employees = res.data;
      const matchingEmployee = employees.find(
        (emp) => emp.userId && emp.userId.id === user.id
      );
      return matchingEmployee ? matchingEmployee.id : null;
    } catch (error) {
      console.error("Lỗi khi lấy employeeId:", error);
      return null;
    }
  };

  const handleFavorite = async (jobId) => {
    if (!user || !user.id) {
      nav("/login");
      return;
    }

    const employeeId = await getEmployeeId();
    if (!employeeId) {
      alert("❌ Không tìm thấy thông tin Employee cho người dùng này!");
      return;
    }

    try {
      const res = await authApis().get(`${endpoints["employeeJob/employee"]}${employeeId}`);
      const allEmployeeJobs = res.data;
      const existingJob = allEmployeeJobs.find(
        (job) => job.employeeId.id === employeeId && job.jobId.id === jobId
      );

      if (existingJob) {
        const newFavoriteJob = existingJob.favoriteJob === 1 ? 0 : 1;
        const response = await authApis().put(
          `${endpoints["employeeJob/employee"]}${employeeId}/${existingJob.id}`,
          {
            jobState: existingJob.jobState,
            favoriteJob: newFavoriteJob
          }
        );
        if (response.status === 200) {
          if (newFavoriteJob === 1) {
            setFavoriteJobs((prev) => [...prev, response.data]);
          } else {
            setFavoriteJobs((prev) => prev.filter((job) => job.id !== existingJob.id));
          }
        }
      } else {
        const newEmployeeJob = {
          employeeId: { id: employeeId },
          jobId: { id: jobId },
          jobState: 0,
          favoriteJob: 1,
        };
        const response = await authApis().post(
          `${endpoints["employeeJob/employee"]}${employeeId}`,
          newEmployeeJob
        );
        if (response.status === 201) {
          setFavoriteJobs((prev) => [...prev, response.data]);
        }
      }
    } catch (error) {
      console.error("Lỗi khi cập nhật trạng thái yêu thích:", error);
      alert("❌ Có lỗi xảy ra khi cập nhật trạng thái yêu thích. Vui lòng thử lại!");
    }
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

  useEffect(() => {
    if (user && user.id) {
      getEmployeeId().then((employeeId) => {
        if (employeeId) {
          loadFavoriteJobs(employeeId);
        }
      });
    }
  }, [user]);

  const loadMore = () => {
    if (!loading && page > 0) {
      setPage((prev) => prev + 1);
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

  const handleApply = async (jobId) => {
    if (!user || !user.id) {
      nav("/login");
      return;
    }

    try {
      const res = await authApis().get(endpoints.employees);
      const employees = res.data;
      const matchingEmployee = employees.find(
        (emp) => emp.userId && emp.userId.id === user.id
      );
      if (matchingEmployee) {
        const employeeId = matchingEmployee.id;
        nav(`/Apply/${employeeId}/${jobId}`);
      } else {
        alert("❌ Không tìm thấy thông tin Employee cho người dùng này!");
      }
    } catch (error) {
      console.error("Error loading employeeId:", error);
      alert("❌ Không thể tải thông tin Employee. Vui lòng thử lại!");
    }
  };

  const approvedJobs = Array.isArray(jobPostings) ? jobPostings.filter((job) => job.state === "approved") : [];

  return (
    <div className="home-container">
      <div className="filter-section">
        <form onSubmit={search} className="filter-form">
          {[
            { label: "Tìm công việc", placeholder: "Nhập tên công việc", name: "kw", type: "search" },
            { label: "Địa điểm", placeholder: "Nhập địa điểm", name: "location", type: "search" },
            { label: "Lương tối thiểu ($)", placeholder: "VD: 1000", name: "salary", type: "number" },
            { label: "Tên công ty", placeholder: "VD: FPT", name: "companyName", type: "search" },
          ].map(({ label, placeholder, name, type }) => (
            <div className="filter-group" key={name}>
              <label>{label}</label>
              <input
                type={type}
                placeholder={placeholder}
                value={filters[name]}
                onChange={(e) => setFilters({ ...filters, [name]: e.target.value })}
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
          const isFavorite = favoriteJobs.some((favJob) => favJob.jobId.id === job.id);

          return (
            <div key={job.id} className="job-card" onClick={() => nav(`/job_detail/${job.id}`)} style={{ cursor: "pointer" }}>
              <img src={companyInfo.imagePath} alt={companyInfo.name} className="job-logo" onError={(e) => { e.target.src = ou; }} />
              <div className="job-content">
                <h3 className="job-title job-title-link">{job.name || "Tên công việc"}</h3>
                <p className="job-details">
                  💰 {job.salary ? `Lương: ${job.salary} $` : "Lương: Thỏa thuận"} -
                  <img src={iconCate} alt="Loại công việc" style={{ width: "20px", height: "20px", margin: "7px", verticalAlign: "middle" }} />
                  Loại công việc: {job.categoryId?.name || "Chưa xác định"}
                </p>
                <p className="job-time">
                  🕒 Bắt đầu: {formatTime(job.timeStart)} - Kết thúc: {formatTime(job.timeEnd)}
                </p>
                <p className="company-info">
                  🏢 Công ty:
                  <Link to={`/company_info/${companyInfo.id}`} onClick={(e) => e.stopPropagation()} className="company-link">
                    {companyInfo.name}
                  </Link>
                  - 📍 Địa chỉ: {companyInfo.address}
                </p>
              </div>
              {user && user.role === "ROLE_EMPLOYEE" && (
                <div className="action-buttons" onClick={(e) => e.stopPropagation()}>
                  <button className="apply-btn" onClick={() => handleApply(job.id)}>Ứng tuyển</button>
                  <span className="heart-icon" style={{ cursor: "pointer", color: isFavorite ? "red" : "black" }} onClick={() => handleFavorite(job.id)}>
                    {isFavorite ? "♥" : "♡"}
                  </span>
                </div>
              )}
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

import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import Api, { endpoints } from "../configs/Api";
import ou from "../img/ou.png";
import '../static/home.css';

const Home = () => {
    const [jobPostings, setJobPostings] = useState([]);
    const [page, setPage] = useState(1);
    const [loading, setLoading] = useState(false);
    const [q] = useSearchParams();

    const loadJob = async () => {
        if (page > 0) {
            try {
                setLoading(true);
                let url = `${endpoints['job_postings']}?page=${page}`;
                let cateId = q.get("categoryId");
                if (cateId) {
                    url = `${url}&categoryId=${cateId}`;
                }
                let kw = q.get('kw');
                if (kw) {
                    url = `${url}&kw=${kw}`;
                }
                let res = await Api.get(url);
                if (res.data.length === 0) {
                    setPage(0);
                } else {
                    if (page === 1) {
                        setJobPostings(res.data);
                    } else {
                        setJobPostings([...jobPostings, ...res.data]);
                    }
                }
            } catch {
                // Xử lý lỗi
            } finally {
                setLoading(false);
            }
        }
    };

    useEffect(() => {
        loadJob();
    }, [page, q]);

    useEffect(() => {
        setPage(1);
        setJobPostings([]);
    }, [q]);

    const loadMore = () => {
        if (!loading && page > 0) {
            setPage(page + 1);
        }
    };

    // Lọc các job có trạng thái "approved"
    const approvedJobs = jobPostings.filter(job => job.state === "approved");

    // Hàm format thời gian HH:mm:ss thành HH:mm
    const formatTime = (time) => {
        if (!time) return "Chưa xác định";
        // Lấy phần HH:mm, bỏ ss
        return time.split(':').slice(0, 2).join(':');
    };

    // Hàm format ngày từ timestamp
    const formatDate = (timestamp) => {
        if (!timestamp) return "Chưa xác định";
        const date = new Date(timestamp);
        return isNaN(date.getTime()) ? "Chưa xác định" : date.toLocaleDateString();
    };

    return (
        <>
            <div className="home-container">
                {approvedJobs.length > 0 ? (
                    approvedJobs.map((job) => (
                        <div key={job.id} className="job-card">
                            <img
                                src={ou}
                                alt={job.employerId?.name || "Company Logo"}
                                className="job-logo"
                            />
                            <div className="job-content">
                                <h3 className="job-title">{job.description || "Mô tả công việc"}</h3>
                                <p className="job-details">
                                    💰 {job.salary ? `Lương: ${job.salary} $` : "Lương: Thỏa thuận"} - 
                                    📅 {job.submitEnd ? `Hạn nộp: ${formatDate(job.submitEnd)}` : "Hạn nộp: Chưa xác định"}
                                </p>
                                <p className="job-time">
                                    🕒 Bắt đầu: {formatTime(job.timeStart)} - 
                                    Kết thúc: {formatTime(job.timeEnd)}
                                </p>
                            </div>
                            <div className="action-buttons">
                                <button className="apply-btn">Ứng tuyển</button>
                                <span className="heart-icon">♡</span>
                            </div>
                        </div>
                    ))
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
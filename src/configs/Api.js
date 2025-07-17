import axios from "axios";
import cookie from "react-cookies";

/*
 * ===== Backend base URL auto-detect =====
 * Ưu tiên thứ tự:
 * 1. REACT_APP_API_BASE (nếu set lúc build: docker --build-arg / .env)
 * 2. window.__FINDJOB_API_BASE (có thể inject từ index.html nếu cần)
 * 3. window.location.hostname + :8080 + /FindJob/api
 *    - Nếu hostname là localhost/127.0.0.1 -> vẫn dùng localhost
 *    - Nếu là IP LAN (vd 192.168.1.110) -> tự dùng IP đó
 */
function resolveBaseUrl() {
  // 1. ENV build-time
  if (process.env.REACT_APP_API_BASE && process.env.REACT_APP_API_BASE.trim() !== "") {
    return normalizeBase(process.env.REACT_APP_API_BASE.trim());
  }

  // 2. Global override runtime (optional)
  if (typeof window !== "undefined" && window.__FINDJOB_API_BASE) {
    return normalizeBase(window.__FINDJOB_API_BASE);
  }

  // 3. Auto build từ hostname hiện tại
  let host = "localhost";
  if (typeof window !== "undefined") {
    host = window.location.hostname || "localhost";
  }

  const port = 8080;                // nếu backend đổi port, sửa ở đây
  const path = "/FindJob/api";      // path cố định backend

  return `http://${host}:${port}${path}/`;
}

/* bảo đảm luôn kết thúc bằng 1 dấu "/" và không double slash loạn */
function normalizeBase(u) {
  // bỏ slash cuối cùng nếu có rồi thêm lại 1
  return u.replace(/\/+$/,"") + "/";
}

const BASE_URL = resolveBaseUrl();

/* ---------------- Endpoints ----------------
 * Giữ nguyên cấu trúc bạn đang dùng.
 * Một số có "/" đầu, một số không; axios sẽ tự nối OK dù thành "//"
 * Nếu muốn sạch hơn thì chuẩn hóa sau.
 */
export const endpoints = {
  categories: '/categories',
  job_postings: 'job_postings',
  company_info: 'company_info',
  company_images: 'company_images',
  job_details: 'job_details',
  job_details_by_job_posting: 'job_details/jobPosting',
  employers: '/employers',
  employees: '/employees',
  employeeJob_employee: 'employeeJob/employee/',
  login: '/login',
  profile: '/secure/profile',
  oauth: '/oauth-login',
  users: '/users',
  updated: '/user/update',
  documentsByUser: 'user_documents/by_user',
  updateDocument: '/user_documents',
  checkEmailExists: '/users/check_email_exists',
  followCount: '/follow-count',
  employeeFromUser: '/employee/from_user',
  follow: '/follows',
  followExists: (employeeId, employerId) => `follows/follow-exists/${employeeId}/${employerId}`,
  unfollow: (employeeId, employerId) => `/follows/${employeeId}/${employerId}`,
  unreadNotificationCount: "/notifications/unread_count",
  notificationsByEmployee: "/notifications/by_employee",
  markNotificationRead: "/notifications/read",
  addDocumentForEmployee: (employeeId) => `user_documents/employee/${employeeId}`,
};

/* axios instance không auth */
const api = axios.create({
  baseURL: BASE_URL,
});

/* axios instance có auth header */
export const authApis = () => {
  return axios.create({
    baseURL: BASE_URL,
    headers: {
      'Authorization': `Bearer ${cookie.load('token')}`,
      'Content-Type': 'application/json'
    }
  });
};

export default api;


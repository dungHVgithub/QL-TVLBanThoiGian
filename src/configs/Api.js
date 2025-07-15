import axios from "axios";
import cookie from "react-cookies"

const BASE_URL = 'http://localhost:8080/FindJob/api/';

export const endpoints = {
    'categories': '/categories',
    'job_postings': 'job_postings',
    'company_info': 'company_info',
    'company_images': 'company_images',
    'job_details': 'job_details',
    'job_details_by_job_posting': 'job_details/jobPosting',
    'employers': '/employers',
    'employees': '/employees',
    'employeeJob/employee': 'employeeJob/employee/',
    'login': '/login',
    'profile': '/secure/profile',
    'oauth': '/oauth-login',
    'users': '/users',
    'updated': '/user/update',
    'documentsByUser': 'user_documents/by_user',
    'updateDocument': '/user_documents',
    'checkEmailExists': '/users/check_email_exists',
    'followCount': '/follow-count',
    'employeeFromUser': '/employee/from_user',
    'follow': '/follows',
    'followExists': (employeeId, employerId) => `follows/follow-exists/${employeeId}/${employerId}`,
    'unfollow': (employeeId, employerId) => `/follows/${employeeId}/${employerId}`,
    'unreadNotificationCount': "/notifications/unread_count",
    'notificationsByEmployee': "/notifications/by_employee",
    "markNotificationRead": "/notifications/read",
    'addDocumentForEmployee': 'user_documents/employee/{employeeId}'

}

export const authApis = () => {
    return axios.create({
        baseURL: BASE_URL,
        headers: {
            'Authorization': `Bearer ${cookie.load('token')}`,
            'Content-Type': 'application/json'
        }
    })
}
export default axios.create({
    baseURL: BASE_URL
});
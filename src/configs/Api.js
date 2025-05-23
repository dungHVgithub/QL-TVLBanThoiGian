import axios from "axios";
import cookie from "react-cookies"

const BASE_URL = 'http://localhost:8080/FindJob/api/';

export const endpoints = {
    'categories' : '/categories',
    'job_postings' : 'job_postings',
    'company_info' : 'company_info',
    'company_images' : 'company_images',
    'job_details' : 'job_details',
    'job_details_by_job_posting' : 'job_details/jobPosting',  
    'employers': '/employers',
    'register': '/users',
    'login': '/login',
    'profile': '/secure/profile',
    'oauth':'/oauth-login',
    'users': '/users',
    'updated': '/user/update',
}

export const authApis = () => {
    console.info(cookie.load('token'));
    return axios.create({
        baseURL: BASE_URL,
        headers: {
            'Authorization': `Bearer ${cookie.load('token')}`,
            'Content-Type': 'application/json'
        }
    })
}
export default axios.create({
    baseURL : BASE_URL
});
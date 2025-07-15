import { BrowserRouter, Route, Routes } from "react-router-dom";
import Header from "./components/layouts/Header";
import Footer from "./components/layouts/Footer";
import Home from "./components/Home";
import 'bootstrap/dist/css/bootstrap.min.css';
import { MyUserContext, MyDispatchContext } from "./configs/MyContexts";
import MyUserReducer from "./reducers/MyUserReducer";
import {  NotificationContext } from "./configs/MyContexts";
import { Container } from "react-bootstrap";
import { useEffect, useReducer, useState } from "react";
import Login from "./components/Login";
import Register from "./components/Register";
import Employer from "./components/Employer";
import JobDetail from "./components/JobDetail";
import Api, { authApis, endpoints } from "./configs/Api";
import Profile from "./components/Profile";
import cookie from "react-cookies";
import CompanyInfo from "./components/CompanyInfo";
import NotificationList from "./components/NotificationList";
import PostList from "./components/postList";
import UpdateJob from "./components/updateJob";
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import Apply from "./components/Apply";
import Employee from "./components/employee";
import FavoriteJob from "./components/favoriteJob"; // Import component


const App = () => {
  const [user, dispatch] = useReducer(MyUserReducer, null);
  const [unreadCount, setUnreadCount] = useState(0); // ✅

  useEffect(() => {
    const init = async () => {
      let token = localStorage.getItem("token") || cookie.load("token");
      if (token) {
        try {
          const res = await authApis().get(endpoints['profile']);
          dispatch({
            type: "login",
            payload: {
              id: res.data.id,
              token: token,
              username: res.data.username,
              name: res.data.name,
              role: res.data.role
            }
          });
        } catch (err) {
          console.error("Token lỗi hoặc hết hạn:", err);
          localStorage.removeItem("token");
          cookie.remove("token");
        }
      }
    };

    init();
  }, []);

  return (
    <MyUserContext.Provider value={user}>
      <MyDispatchContext.Provider value={dispatch}>
        <BrowserRouter>
          <Header />
          <Container>
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/register" element={<Register />} />
              <Route path="/login" element={<Login />} />
              <Route path="/employer" element={<Employer />} />
              <Route path="/profile" element={<Profile />} />
              <Route path="/job_detail/:id" element={<JobDetail />} />
              <Route path="company_info/:companyId" element={<CompanyInfo />} />
              <Route path="/notifications" element={<NotificationList />} />
              <Route path="postList/:employerId" element={<PostList />} />
              <Route path="updateJob/:jobId" element={<UpdateJob />} />
              <Route path="Apply/:employeeId/:jobId" element={<Apply />} />
              <Route path="employee/:employeeId" element={<Employee />} />
              <Route path="favoriteJob/:employeeId" element={<FavoriteJob />} /> {/* Route đúng */}
            </Routes>
          </Container>
          <Footer />
          <ToastContainer
            position="top-right"
            autoClose={3000}
            hideProgressBar={false}
            newestOnTop={false}
            closeOnClick
            rtl={false}
            pauseOnFocusLoss
            draggable
            pauseOnHover
            theme="light"
          />
        </BrowserRouter>
      </MyDispatchContext.Provider>
    </MyUserContext.Provider>
  );
};

export default App;

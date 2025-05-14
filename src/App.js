import { BrowserRouter, Route, Routes } from "react-router-dom";
import Header from "./components/layouts/Header";
import Footer from "./components/layouts/Footer";
import Home from "./components/Home";
import 'bootstrap/dist/css/bootstrap.min.css';
import { MyUserContext } from "./configs/MyContexts";
import MyUserReducer from "./reducers/MyUserReducer";
import { Container } from "react-bootstrap";
import { useEffect, useReducer } from "react";
import { MyDispatchContext } from "./configs/MyContexts";
import Login from "./components/Login";
import Register from "./components/Register";
import Employer from "./components/Employer";
import Api, { endpoints } from "./configs/Api";
const App = () => {
  const [user, dispatch] = useReducer(MyUserReducer, null);
  useEffect(() => {
    const init = async () => {
      const token = localStorage.getItem("token");
      if (token) {
        try {
          const res = await Api.get(endpoints['profile'], {
            headers: {
              Authorization: `Bearer ${token}`
            }
          });

          dispatch({
            type: "login",
            payload: {
              token: token,
              username: res.data.username,
              role: res.data.role
            }
          });
        } catch (err) {
          console.error("Token lỗi hoặc hết hạn:", err);
          localStorage.removeItem("token");
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
            </Routes>
          </Container>
          <Footer />
        </BrowserRouter>
      </MyDispatchContext.Provider>
    </MyUserContext.Provider>
  );
}
export default App;
import { useEffect, useState, useContext } from 'react';
import cookie from "react-cookies";
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import { Link } from 'react-router-dom';
import Api, { authApis, endpoints } from '../../configs/Api';
import '../../static/header.css';
import { Button } from 'react-bootstrap';
import { MyDispatchContext, MyUserContext } from "../../configs/MyContexts";
import { FaBell, FaUserCircle } from 'react-icons/fa';


const Header = () => {
  const [categories, setCategories] = useState([]);
  const user = useContext(MyUserContext);
  const dispatch = useContext(MyDispatchContext);
  const [unreadCount, setUnreadCount] = useState(0);

  const loadCates = async () => {
    try {
      let res = await Api.get(endpoints['categories']);
      setCategories(res.data);
    } catch (error) {
      console.error("Error loading categories:", error);
    }
  };

  useEffect(() => {
    loadCates();

    const loadUnread = async () => {
      try {
        if (user?.role === "ROLE_EMPLOYEE") {
          // Lấy employeeId từ userId
          const empRes = await authApis().get(`${endpoints["employeeFromUser"]}/${user.id}`);
          const employeeId = empRes.data;

          // Lấy số thông báo chưa đọc
          const notiRes = await authApis().get(`${endpoints["unreadNotificationCount"]}/${employeeId}`);
          setUnreadCount(notiRes.data);
        }
        console.log(">>> user context:", user);
      } catch (err) {
        console.error("❌ Lỗi khi lấy số thông báo chưa đọc:", err);
      }
    };

    loadUnread();
  }, [user]);

  return (
    <Navbar expand="lg" className="bg-body-tertiary" aria-label="Main navigation">
      <Container fluid>
        <Navbar.Brand href="#">Find Job</Navbar.Brand>
        <Navbar.Toggle aria-controls="navbarScroll" />
        <Navbar.Collapse id="navbarScroll">
          {/* Bên trái */}
          <Nav className="me-auto d-flex align-items-center">
            <Link to="/" className="nav-link">Trang chủ</Link>
            <NavDropdown title="Danh mục" id="navbarScrollingDropdown">
              {categories.map(c => {
                let url = `/?categoryId=${c.id}`;
                return <Link className="dropdown-item" key={c.id} to={url}>{c.name}</Link>;
              })}
            </NavDropdown>
            {user && user.role === "ROLE_EMPLOYER" && (
              <Link to="/employer" className="nav-link">Đăng tin tuyển dụng</Link>
            )}
          </Nav>

          {/* Bên phải */}
          <Nav className="ms-auto d-flex align-items-center">
            {user === null ? (
              <>
                <Link to="/register" className="nav-link text-success">Đăng ký</Link>
                <Link to="/login" className="nav-link text-danger">Đăng nhập</Link>
              </>
            ) : (
              <>

                {user && user.role === "ROLE_EMPLOYEE" && (
                  <>
                    <Link to="/notifications" className="nav-link text-success"><div className="d-flex align-items-center position-relative">
                      <FaBell size={20} />
                      <span className="ms-1">Thông Báo Việc Làm</span>
                      {unreadCount > 0 && (
                        <span style={{
                          position: "absolute",
                          top: -5,
                          right: -10,
                          backgroundColor: "red",
                          color: "white",
                          borderRadius: "50%",
                          padding: "2px 6px",
                          fontSize: "0.7rem"
                        }}>
                          {unreadCount}
                        </span>
                      )}
                    </div>


                    </Link>


                  </>
                )}

                <div className="vr mx-2" />

                {/* Icon người dùng */}
                <div className="d-flex align-items-center">
                  <FaUserCircle size={20} />
                  <Link to="/profile" className="nav-link text-primary fw-bold mb-0 ms-1">Chào {user.name}</Link>

                </div>

                <Link to="/" className="nav-link text-success">
                  <Button variant="outline-danger" size="sm" onClick={() => {
                    dispatch({ type: "logout" });
                    cookie.remove("token");
                    localStorage.removeItem("token");
                  }}>
                    Đăng xuất
                  </Button>
                </Link>
              </>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default Header;

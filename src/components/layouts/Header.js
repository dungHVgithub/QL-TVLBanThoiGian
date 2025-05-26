import { useEffect, useState, useContext } from 'react';
import cookie from "react-cookies";
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import { Link, useNavigate } from 'react-router-dom';
import Api, { authApis, endpoints } from '../../configs/Api';
import '../../static/header.css';
import { Button } from 'react-bootstrap';
import { MyDispatchContext, MyUserContext } from "../../configs/MyContexts";
import { FaBell, FaUserCircle } from 'react-icons/fa';

const Header = () => {
  const [categories, setCategories] = useState([]);
  const user = useContext(MyUserContext);
  const dispatch = useContext(MyDispatchContext);
  const navigate = useNavigate();

  const loadCates = async () => {
    try {
      let res = await Api.get(endpoints['categories']);
      setCategories(res.data);
    } catch (error) {
      console.error("Error loading categories:", error);
    }
  };

  const loadEmployerId = async () => {
    if (user && user.role === "ROLE_EMPLOYER" && user.id) {
      try {
        const employerRes = await authApis().get(endpoints["employers"]);
        const matchedEmployer = employerRes.data.find(
          (employer) => employer.userId.id === user.id
        );
        if (matchedEmployer) {
          return matchedEmployer.id;
        } else {
          console.error("Không tìm thấy employer khớp với user.id:", user.id);
          alert("Không thể tìm thấy thông tin nhà tuyển dụng. Vui lòng thử lại sau.");
          return null;
        }
      } catch (error) {
        console.error("Không thể tải thông tin nhà tuyển dụng:", error);
        alert("Có lỗi xảy ra khi lấy thông tin nhà tuyển dụng.");
        return null;
      }
    } else {
      alert("Bạn cần đăng nhập với vai trò nhà tuyển dụng để xem bài đăng.");
      return null;
    }
  };

  const loadEmployeeId = async () => {
    if (user && user.role === "ROLE_EMPLOYEE" && user.id) {
      try {
        const employeeRes = await authApis().get(endpoints["employees"]);
        console.info(employeeRes.data);
        // Tìm employee có userId.id khớp với user.id
        const matchedEmployee = employeeRes.data.find(
          (employee) => employee.userId.id === user.id
        );
        if (matchedEmployee) {
          return matchedEmployee.id; // Trả về employeeId
        } else {
          console.error("Không tìm thấy employee khớp với user.id:", user.id);
          alert("Không thể tìm thấy thông tin nhân viên. Vui lòng thử lại sau.");
          return null;
        }
      } catch (error) {
        console.error("Không thể tải thông tin nhân viên:", error);
        alert("Có lỗi xảy ra khi lấy thông tin nhân viên.");
        return null;
      }
    } else {
      alert("Bạn cần đăng nhập với vai trò nhân viên để xem công việc ứng tuyển.");
      return null;
    }
  };

  useEffect(() => {
    loadCates();
  }, []);

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
            {user && user.role === "ROLE_EMPLOYER" && (
              <Link
                to="/postList"
                className="nav-link"
                onClick={async (e) => {
                  e.preventDefault();
                  const employerId = await loadEmployerId();
                  if (employerId) {
                    navigate(`/postList/${employerId}`);
                  }
                }}
              >
                Bài đăng của bạn
              </Link>
            )}
            {user && user.role === "ROLE_EMPLOYEE" && (
              <Link
                to="/employee"
                className="nav-link"
                onClick={async (e) => {
                  e.preventDefault();
                  const employeeId = await loadEmployeeId();
                  if (employeeId) {
                    navigate(`/employee/${employeeId}`);
                  }
                }}
              >
                Công việc ứng tuyển
              </Link>
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
                <div className="d-flex align-items-center position-relative">
                  <FaBell size={20} />
                  <span className="ms-1">Thông Báo Việc Làm</span>
                  <span className="position-absolute top-0 start-100 translate-middle p-1 bg-danger border border-light rounded-circle" style={{ width: '8px', height: '8px' }}></span>
                </div>
                <div className="vr mx-2" />
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
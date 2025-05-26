import { useEffect, useState, useContext } from 'react';
import cookie from "react-cookies"
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import { Link } from 'react-router-dom';
import Api, { endpoints } from '../../configs/Api';
import '../../static/header.css';
import { Button } from 'react-bootstrap';
import { MyDispatchContext, MyUserContext } from "../../configs/MyContexts";
import { FaBell, FaUserCircle } from 'react-icons/fa';

const Header = () => {
  const [categories, setCategories] = useState([]);
  const user = useContext(MyUserContext);
  const dispatch = useContext(MyDispatchContext);

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
                {/* Icon chuông + Thông báo */}
                <div className="d-flex align-items-center position-relative">
                  <FaBell size={20} />
                  <span className="ms-1">Thông Báo Việc Làm</span>
                  <span className="position-absolute top-0 start-100 translate-middle p-1 bg-danger border border-light rounded-circle" style={{ width: '8px', height: '8px' }}></span>
                </div>

                {/* Divider */}
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
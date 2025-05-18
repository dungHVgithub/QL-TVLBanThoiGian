import { useEffect, useState,useContext } from 'react';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import { Link } from 'react-router-dom';
import Api, { endpoints } from '../../configs/Api';
import '../../static/header.css'; // Điều chỉnh đường dẫn để import header.css từ thư mục gốc
import { Button } from 'react-bootstrap';
import { MyDispatchContext, MyUserContext } from "../../configs/MyContexts";




const Header = () => {
  const [categories, setCategories] = useState([]);
  const user = useContext(MyUserContext);
  const dispatch = useContext(MyDispatchContext);

  const loadCates = async () => {
    try {
      let res = await Api.get(endpoints['categories']);
      setCategories(res.data);
      console.info(res.data);
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
        <Navbar.Brand href="#"> Find Job </Navbar.Brand>
        <Navbar.Toggle aria-controls="navbarScroll" />
        <Navbar.Collapse id="navbarScroll">
          <Nav
            className="me-auto my-2 my-lg-0"
            style={{ maxHeight: '100px' }}
            navbarScroll
          >

            <Link to="/" className="nav-link">Trang chủ</Link>
            <NavDropdown title="Danh mục" id="navbarScrollingDropdown">
              {categories.map(c => {
                let url = `/?categoryId=${c.id}`
                return <Link className="dropdown-item" key={c.id} to={url}>{c.name}</Link>;
              })}
            </NavDropdown>
            {user===null?<>
                        <Link to="/register" className="nav-link text-success">Đăng ký</Link>
                        <Link to="/login" className="nav-link text-danger">Đăng nhập</Link>
                    </>:<>
                        <Link to="/" className="nav-link text-success">Chào {user.name}!</Link>
                        <Button className="btn btnd-danger" onClick={() => dispatch({"type": "logout"})}>Đăng xuất</Button>
                    </>}
          </Nav>
          
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};
export default Header;
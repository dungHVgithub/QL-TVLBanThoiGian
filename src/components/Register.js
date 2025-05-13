import { useRef, useState } from "react";
import { Alert, Button, FloatingLabel, Form, Col, Row } from "react-bootstrap";
import Api, { endpoints } from "../configs/Api";
import { useNavigate } from "react-router-dom";
import MySpinner from "./layouts/MySpinner";

const Register = () => {
    const info = [
        {
            label: "Họ Tên",
            type: "text",
            field: "name"
        }, 
        {
            label: "Tên đăng nhập",
            type: "text", 
            field: "username"
        }, 
        {
            label: "Mật khẩu",
            type: "password", 
            field: "password"
        }, 
        {
            label: "Xác nhận mật khẩu",
            type: "password", 
            field: "confirm"
        }, 
        {
            label: "Điện thoại",
            type: "tel", 
            field: "phone"
        }, 
        {
            label: "Email",
            type: "email", 
            field: "email"
        }
    ];
    const avatar = useRef();
    const [msg, setMsg] = useState("");
    const [user, setUser] = useState({});
    const [loading, setLoading] = useState(false);
    const nav = useNavigate();

    const setState = (value, field) => {
        setUser({...user, [field]: value});
    };

    const register = async (e) => {
        e.preventDefault();

        if (user.password !== user.confirm) {
            setMsg("Mật khẩu không khớp!");
        } else {
            try {
                setLoading(true);
                let form = new FormData();
                for (let f of info) {
                    if (f.field !== 'confirm') {
                        form.append(f.field, user[f.field]);
                    }
                }
                form.append('role', user.role);
                form.append('avatar', avatar.current.files[0]);
                
                let res = await Api.post(endpoints['register'], form, {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                });
                if (res.status === 201) {
                    nav("/login");
                }
            } catch {
                setMsg("Đăng ký thất bại. Vui lòng thử lại!");
            } finally {
                setLoading(false);
            }
        }
    };

    return (
        <div className="container mt-4">
            <h1 className="text-center text-success mb-4">ĐĂNG KÝ NGƯỜI DÙNG</h1>

            {msg && <Alert variant="danger">{msg}</Alert>}

            <Form onSubmit={register} className="bg-light p-4 rounded shadow">
                <Row>
                    {info.map((f, index) => (
                        <Col md={6} key={f.field} className="mb-3">
                            <FloatingLabel controlId={`floating${f.field}`} label={f.label}>
                                <Form.Control 
                                    type={f.type} 
                                    placeholder={f.label} 
                                    required 
                                    value={user[f.field] || ''} 
                                    onChange={e => setState(e.target.value, f.field)} 
                                />
                            </FloatingLabel>
                        </Col>
                    ))}
                </Row>

                <Row className="mb-3">
                    <Col md={6}>
                        <Form.Group controlId="roleSelection">
                            <Form.Label>Quyền</Form.Label>
                            <div>
                                <Form.Check
                                    inline
                                    type="radio"
                                    label="Nhân viên"
                                    name="role"
                                    value="ROLE_EMPLOYEE"
                                    checked={user.role === "ROLE_EMPLOYEE"}
                                    onChange={e => setState(e.target.value, "role")}
                                    required
                                />
                                <Form.Check
                                    inline
                                    type="radio"
                                    label="Nhà tuyển dụng"
                                    name="role"
                                    value="ROLE_EMPLOYER"
                                    checked={user.role === "ROLE_EMPLOYER"}
                                    onChange={e => setState(e.target.value, "role")}
                                    required
                                />
                            </div>
                        </Form.Group>
                    </Col>
                    <Col md={6}>
                        <FloatingLabel controlId="floatingAvatar" label="Ảnh đại diện">
                            <Form.Control 
                                type="file" 
                                placeholder="Ảnh đại diện" 
                                ref={avatar} 
                            />
                        </FloatingLabel>
                    </Col>
                </Row>

                <div className="text-center">
                    {loading ? (
                        <MySpinner />
                    ) : (
                        <Button 
                            type="submit" 
                            variant="success" 
                            size="lg"
                            className="px-5"
                        >
                            Đăng ký
                        </Button>
                    )}
                </div>
            </Form>
        </div>
    );
};

export default Register;
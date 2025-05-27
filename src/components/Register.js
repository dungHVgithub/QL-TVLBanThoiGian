import { useRef, useState } from "react";
import {
    Alert,
    Button,
    FloatingLabel,
    Form,
    Col,
    Row,
    ProgressBar
} from "react-bootstrap";
import Api, { endpoints } from "../configs/Api";
import { useNavigate } from "react-router-dom";
import MySpinner from "./layouts/MySpinner";

const Register = () => {
    const info = [
        { label: "Họ Tên", type: "text", field: "name" },
        { label: "Tên đăng nhập", type: "text", field: "username" },
        { label: "Mật khẩu", type: "password", field: "password" },
        { label: "Xác nhận mật khẩu", type: "password", field: "confirm" },
        { label: "Điện thoại", type: "tel", field: "sdt" },
        { label: "Email", type: "email", field: "email" },
        { label: "Địa chỉ", type: "text", field: "address" },
    ];

    const avatar = useRef();
    const [msg, setMsg] = useState("");
    const [user, setUser] = useState({});
    const [loading, setLoading] = useState(false);
    const nav = useNavigate();

    const setState = (value, field) => setUser({ ...user, [field]: value });

    const validate = () => {
        const nameRegex = /^[a-zA-ZÀÁÃẠẢĂẰẮẴẶẲÂẦẤẪẬẨÈÉẼẸẺÊỀẾỄỆỂÌÍĨỊỈÒÓÕỌỎÔỒỐỖỘỔƠỜỚỠỢỞÙÚŨỤỦƯỪỨỮỰỬỲÝỸỴỶĐđ\s]+$/u;
        const usernameRegex = /^\S+$/;
        const phoneRegex = /^0\d{9}$/;

        if (!nameRegex.test((user.name || "").trim())) {
            setMsg("❌ Họ tên không hợp lệ! Không được chứa số, ký tự đặc biệt hoặc khoảng trắng đầu/cuối.");
            return false;
        }
        if (!usernameRegex.test(user.username || "")) {
            setMsg("❌ Tên đăng nhập không được chứa khoảng trắng!");
            return false;
        }
        if ((user.password || "").trim() !== (user.confirm || "").trim()) {
            setMsg("❌ Mật khẩu không khớp!");
            return false;
        }
        if (!phoneRegex.test(user.sdt || "")) {
            setMsg("❌ Số điện thoại phải bắt đầu bằng số 0 và gồm 10 chữ số!");
            return false;
        }
        if (!avatar.current || avatar.current.files.length === 0) {
            setMsg("❌ Vui lòng chọn ảnh đại diện!");
            return false;
        }
        return true;
    };

    const getPasswordStrength = (password) => {
        if (!password) return 0;
        let score = 0;
        if (password.length >= 6) score++;
        if (/[a-z]/.test(password)) score++;
        if (/[A-Z]/.test(password)) score++;
        if (/[^a-zA-Z0-9]/.test(password)) score++;
        return score;
    };

    const passwordStrength = getPasswordStrength(user.password);

    const register = async (e) => {
        e.preventDefault();

        if (!validate()) return;

        try {
            setLoading(true);
            let form = new FormData();
            for (let f of info) {
                if (f.field !== "confirm") form.append(f.field, user[f.field]);
            }
            form.append("role", user.role);
            if (avatar.current.files.length > 0)
                form.append("avatar", avatar.current.files[0]);

            const res = await Api.post(endpoints.users, form, {
                headers: { "Content-Type": "multipart/form-data" },
            });

            if (res.status === 201) {
                alert("✅ Đăng ký thành công! Vui lòng đăng nhập.");
                nav("/login");
            }
        } catch (err) {
            console.error("Đăng ký thất bại:", err.response?.data || err.message);
            setMsg("❌ Đăng ký thất bại. Vui lòng thử lại sau!");
        } finally {
            setLoading(false);
        }
    };

    const getPasswordVariant = () => {
        switch (passwordStrength) {
            case 1: return "danger";
            case 2: return "warning";
            case 3:
            case 4: return "success";
            default: return "danger";
        }
    };

    const getPasswordLabel = () => {
        switch (passwordStrength) {
            case 1: return "Yếu";
            case 2: return "Trung bình";
            case 3:
            case 4: return "Mạnh";
            default: return "";
        }
    };

    return (
        <div className="container mt-5">
            <div className="mx-auto shadow rounded p-4 bg-white" style={{ maxWidth: "800px" }}>
                <h2 className="text-center text-success mb-4">ĐĂNG KÝ NGƯỜI DÙNG</h2>

                {msg && <Alert variant="danger">{msg}</Alert>}

                <Form onSubmit={register}>
                    <Row>
                        {info.map((f) => (
                            <Col md={6} key={f.field} className="mb-3">
                                <FloatingLabel controlId={`floating${f.field}`} label={f.label}>
                                    <Form.Control
                                        type={f.type}
                                        placeholder={f.label}
                                        required
                                        value={user[f.field] || ""}
                                        onChange={(e) => setState(e.target.value, f.field)}
                                    />
                                </FloatingLabel>
                                {f.field === "password" &&
                                    <ProgressBar 
                                        variant={getPasswordVariant()} 
                                        now={passwordStrength * 25} 
                                        label={getPasswordLabel()} 
                                        className="mt-2" />
                                }
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
                                        id="role-employee"
                                        label="Nhân viên"
                                        name="role"
                                        value="ROLE_EMPLOYEE"
                                        checked={user.role === "ROLE_EMPLOYEE"}
                                        onChange={(e) => setState(e.target.value, "role")}
                                        required
                                    />
                                    <Form.Check
                                        inline
                                        type="radio"
                                        id="role-employer"
                                        label="Nhà tuyển dụng"
                                        name="role"
                                        value="ROLE_EMPLOYER"
                                        checked={user.role === "ROLE_EMPLOYER"}
                                        onChange={(e) => setState(e.target.value, "role")}
                                        required
                                    />
                                </div>
                            </Form.Group>
                        </Col>

                        <Col md={6}>
                            <FloatingLabel controlId="floatingAvatar" label="Ảnh đại diện">
                                <Form.Control type="file" placeholder="Ảnh đại diện" ref={avatar} />
                            </FloatingLabel>
                        </Col>
                    </Row>

                    <div className="text-center">
                        {loading ? (
                            <MySpinner />
                        ) : (
                            <Button type="submit" variant="success" size="lg" className="px-5">
                                Đăng ký
                            </Button>
                        )}
                    </div>
                </Form>
            </div>
        </div>
    );
};

export default Register;
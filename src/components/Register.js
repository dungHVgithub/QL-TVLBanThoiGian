import { useRef, useState } from "react";
import { Alert, Button, FloatingLabel, Form, Col, Row, ProgressBar } from "react-bootstrap";
import Api, { endpoints } from "../configs/Api";
import { useNavigate } from "react-router-dom";
import MySpinner from "./layouts/MySpinner";
import { auth, googleProvider, facebookProvider } from "../configs/FirebaseConfig";
import { signInWithPopup } from "firebase/auth";
import { FaFacebook, FaGoogle } from "react-icons/fa";
import cookie from "react-cookies";
import SelectRoleModal from "./SelectRoleModal";

const Register = () => {
    const info = [
        { label: "Họ Tên", type: "text", field: "name" },
        { label: "Tên đăng nhập", type: "text", field: "username" },
        { label: "Mật khẩu", type: "password", field: "password" },
        { label: "Xác nhận mật khẩu", type: "password", field: "confirm" },
        { label: "Điện thoại", type: "tel", field: "phone" },
        { label: "Email", type: "email", field: "email" }
    ];

    const avatar = useRef();
    const [msg, setMsg] = useState("");
    const [user, setUser] = useState({});
    const [loading, setLoading] = useState(false);
    const [showRoleModal, setShowRoleModal] = useState(false);
    const [tempUserInfo, setTempUserInfo] = useState(null);
    const [passwordStrength, setPasswordStrength] = useState(0);
    const nav = useNavigate();

    const setState = (value, field) => {
        if (field === "phone") {
            if (!/^\d*$/.test(value)) return;
            if (value.length > 0 && value[0] !== "0") return;
        }
        if (field === "name") {
            if (/[^a-zA-ZÀ-ỹ\s]/.test(value)) return;
        }
        if (field === "password") {
            updatePasswordStrength(value);
        }
        setUser({ ...user, [field]: value });
    };

    const updatePasswordStrength = (value) => {
        let score = 0;
        if (value.length >= 6) score += 1;
        if (/[A-Z]/.test(value)) score += 1;
        if (/[0-9]/.test(value)) score += 1;
        if (/[^A-Za-z0-9]/.test(value)) score += 1;
        setPasswordStrength(score);
    };

    const getPasswordStrengthVariant = () => {
        switch (passwordStrength) {
            case 1: return { variant: "danger", label: "Yếu" };
            case 2: return { variant: "warning", label: "Trung bình" };
            case 3:
            case 4: return { variant: "success", label: "Mạnh" };
            default: return { variant: "danger", label: "" };
        }
    };

    const register = async (e) => {
        e.preventDefault();

        if ((user.password || "").trim() !== (user.confirm || "").trim()) {
            setMsg("❌ Mật khẩu không khớp!");
            return;
        }

        try {
            setLoading(true);
            let form = new FormData();

            for (let f of info) {
                if (f.field !== "confirm") form.append(f.field, user[f.field]);
            }
            form.append("role", user.role);
            form.append("avatar", avatar.current.files[0]);

            const res = await Api.post(endpoints.register, form, {
                headers: { "Content-Type": "multipart/form-data" },
            });

            if (res.status === 201) {
                alert("✅ Đăng ký thành công! Vui lòng đăng nhập.");
                nav("/login");
            }
        } catch (err) {
            console.error("Đăng ký thất bại:", err);
            setMsg("❌ Đăng ký thất bại. Vui lòng thử lại sau!");
        } finally {
            setLoading(false);
        }
    };

    const proceedOAuthLogin = async (firebaseUser, role) => {
        const payload = {
            name: firebaseUser.displayName,
            email: firebaseUser.email,
            avatar: firebaseUser.photoURL,
            provider: "google",
            role
        };

        const res = await Api.post(endpoints.oauth, payload);
        const token = res.data.token;
        cookie.save("token", token);
        alert("✅ Đăng ký thành công!");
        alert("✅ Hãy đăng nhập!");
        nav("/login");
    };

    const handleRoleSelect = async (role) => {
        if (tempUserInfo) {
            await proceedOAuthLogin(tempUserInfo.firebaseUser, role);
            setTempUserInfo(null);
        }
    };

    const loginWithProvider = async (provider) => {
        if (loading) return;

        try {
            setLoading(true);
            const result = await signInWithPopup(auth, provider);
            const firebaseUser = result.user;

            let email = firebaseUser.email;
            if (!email && provider === facebookProvider) {
                email = prompt("Facebook không cung cấp email. Vui lòng nhập email để tiếp tục:");
                if (!email.trim()) throw new Error("Email là bắt buộc!");
            }

            const checkRes = await Api.get(`${endpoints.checkEmailExists}?email=${email}`);
            const emailExists = checkRes.data.exists;

            if (emailExists) {
                await proceedOAuthLogin(firebaseUser, "ROLE_EMPLOYEE");
            } else {
                setTempUserInfo({ firebaseUser, provider });
                setShowRoleModal(true);
            }
        } catch (error) {
            console.error("OAuth đăng ký lỗi:", error);
            setMsg("❌ Đăng ký bằng mạng xã hội thất bại!");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container mt-4">
            <h1 className="text-center text-success mb-4">ĐĂNG KÝ NGƯỜI DÙNG</h1>

            {msg && <Alert variant="danger">{msg}</Alert>}

            <div className="text-center mb-4">
                <Button variant="primary" className="me-2" onClick={() => loginWithProvider(facebookProvider)} disabled={loading}>
                    <FaFacebook className="me-2" /> Đăng ký bằng Facebook
                </Button>
                <Button variant="danger" onClick={() => loginWithProvider(googleProvider)} disabled={loading}>
                    <FaGoogle className="me-2" /> Đăng ký bằng Google
                </Button>
            </div>

            <Form onSubmit={register} className="bg-light p-4 rounded shadow">
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
                            {f.field === "password" && user.password && (
                                <ProgressBar
                                    now={passwordStrength * 25}
                                    variant={getPasswordStrengthVariant().variant}
                                    label={getPasswordStrengthVariant().label}
                                    className="mt-1"
                                />
                            )}
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

            <SelectRoleModal
                show={showRoleModal}
                onClose={() => setShowRoleModal(false)}
                onSelect={handleRoleSelect}
            />
        </div>
    );
};

export default Register;

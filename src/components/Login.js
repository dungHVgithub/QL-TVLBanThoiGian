import { useContext, useState } from "react";
import { Button, FloatingLabel, Form, Alert } from "react-bootstrap";
import Api, { authApis, endpoints } from "../configs/Api";
import { MyDispatchContext } from "../configs/MyContexts";
import { useLocation, useNavigate } from "react-router-dom";
import MySpinner from "./layouts/MySpinner";
import { auth, googleProvider, facebookProvider } from "../configs/FirebaseConfig";
import { signInWithPopup } from "firebase/auth";
import { FaFacebook, FaGoogle, FaSignInAlt } from "react-icons/fa";
import cookie from "react-cookies";
import RoleSelectionModal from "./RoleSelectionModal"; // đảm bảo bạn đã có component này

const Login = () => {
    const info = [
        { label: "👤 Tên đăng nhập", type: "text", field: "username" },
        { label: "🔒 Mật khẩu", type: "password", field: "password" }
    ];

    const dispatch = useContext(MyDispatchContext);
    const nav = useNavigate();
    const location = useLocation();
    const [user, setUser] = useState({ username: "", password: "" });
    const [loading, setLoading] = useState(false);
    const [msg, setMsg] = useState("");
    const [googleUser, setGoogleUser] = useState(null);
    const [showRoleModal, setShowRoleModal] = useState(false);
    const [emailExists, setEmailExists] = useState(false);

    const login = async (e) => {
        e.preventDefault();

        if (!user.username || user.username.trim() === "") {
            setMsg("❌ Tên đăng nhập không được để trống!");
            return;
        }

        if (!validateUsername(user.username)) {
            setMsg("❌ Tên đăng nhập chỉ được chứa chữ cái, số và dấu gạch dưới!");
            return;
        }

        if (!user.password || user.password.trim() === "") {
            setMsg("❌ Mật khẩu không được để trống!");
            return;
        }

        try {
            setLoading(true);
            const res = await Api.post(endpoints.login, user);
            const token = res.data.token;

            cookie.save("token", token);
            localStorage.setItem("token", token);
            const profileRes = await authApis().get(endpoints.profile);
            const userInfo = profileRes.data;

            dispatch({
                type: "login",
                payload: {
                    token: token,
                    username: userInfo.username,
                    name: userInfo.name,
                    role: userInfo.role,
                    id: userInfo.id
                },
            });

            if (userInfo.role === "ROLE_EMPLOYEE" || userInfo.role === "ROLE_ADMIN")
                nav("/");
            else if (userInfo.role === "ROLE_EMPLOYER")
                nav("/employer");
            else nav("/");
        } catch (err) {
            console.error("Lỗi đăng nhập:", err);
            setMsg("❌ Đăng nhập thất bại!");
        } finally {
            setLoading(false);
        }
    };

    const setState = (value, field) => {
        setUser({ ...user, [field]: value });
    };

    const loginWithProvider = async (provider) => {
        try {
            setLoading(true);
            setMsg("🔄 Vui lòng không đóng cửa sổ popup cho đến khi hoàn tất đăng nhập!");

            const result = await signInWithPopup(auth, provider);
            const firebaseUser = result.user;

            let email = firebaseUser.email;
            if (!email && provider === facebookProvider) {
                email = prompt("Facebook không cung cấp email. Nhập email để tiếp tục:");
                if (!email) {
                    setMsg("❌ Email bắt buộc để tiếp tục đăng nhập!");
                    throw new Error("Email bắt buộc!");
                }
            }

            const userPayload = {
                name: firebaseUser.displayName,
                email,
                avatar: firebaseUser.photoURL,
            };

            const checkRes = await Api.get(endpoints.checkEmailExists + `?email=${encodeURIComponent(email)}`);
            const exists = checkRes.data.exists;
            setEmailExists(exists);

            if (!exists) {
                setGoogleUser(userPayload);
                setShowRoleModal(true);
            } else {
                const res = await Api.post(endpoints.oauth, userPayload);
                const token = res.data.token;

                cookie.save("token", token);
                localStorage.setItem("token", token);

                const profileRes = await authApis().get(endpoints.profile);
                const userInfo = profileRes.data;

                dispatch({
                    type: "login",
                    payload: {
                        token: token,
                        ...userInfo,
                    },
                });

                handleLoginSuccess(userInfo);
            }
        } catch (err) {
            console.error("❌ OAuth login error:", err);
            setMsg("Đăng nhập mạng xã hội thất bại!");
        } finally {
            setLoading(false);
        }
    };

    const handleRoleSelect = async (role) => {
        try {
            const payload = {
                ...googleUser,
                role: role
            };

            const res = await Api.post(endpoints.oauth, payload);
            const token = res.data.token;

            cookie.save("token", token);
            localStorage.setItem("token", token);

            const profileRes = await authApis().get(endpoints.profile);
            const userInfo = profileRes.data;

            dispatch({
                type: "login",
                payload: {
                    token: token,
                    ...userInfo,
                },
            });

            if (userInfo.role === "ROLE_EMPLOYER") {
                nav("/employer");
            } else {
                nav("/");
            }

        } catch (err) {
            console.error("OAuth login error:", err);
            setMsg("Đăng nhập mạng xã hội thất bại!");
        } finally {
            setLoading(false);
        }
    };

    const handleLoginSuccess = (userInfo) => {
        if (userInfo.role === "ROLE_EMPLOYER") {
            nav("/employer");
        } else {
            nav("/");
        }
    };

    const validateUsername = (username) => /^[a-zA-Z0-9_]+$/.test(username);

    return (
        <div className="login-container">
            <div className="login-card">
                <div className="login-header">
                    <h1 className="login-title">🚀 Chào Mừng Trở Lại</h1>
                    <p className="login-subtitle">Đăng nhập để tiếp tục hành trình của bạn</p>
                </div>

                <Form onSubmit={login} className="login-form">
                    {msg && (
                        <Alert variant={msg.startsWith("✅") ? "success" : "danger"} className="mb-3">
                            {msg}
                        </Alert>
                    )}

                    {info.map((f) => (
                        <div key={f.field} className="login-form-group">
                            <FloatingLabel
                                controlId={`floating-${f.field}`}
                                label={f.label}
                            >
                                <Form.Control
                                    type={f.type}
                                    placeholder={f.label}
                                    value={user[f.field] || ""}
                                    onChange={(e) => setState(e.target.value, f.field)}
                                    required
                                />
                            </FloatingLabel>
                        </div>
                    ))}

                    <div className="login-divider">
                        <span>Chọn phương thức đăng nhập</span>
                    </div>

                    <div className="d-flex justify-content-around mb-3">
                        <Button variant="outline-danger" onClick={() => loginWithProvider(googleProvider)}>
                            <FaGoogle className="me-2" /> Google
                        </Button>
                        <Button variant="outline-primary" onClick={() => loginWithProvider(facebookProvider)}>
                            <FaFacebook className="me-2" /> Facebook
                        </Button>
                    </div>

                    {loading ? (
                        <MySpinner />
                    ) : (
                        <Button type="submit" variant="success" className="mt-1 mb-1">
                            <FaSignInAlt className="me-2" /> Đăng nhập
                        </Button>
                    )}
                </Form>
            </div>

            {showRoleModal && (
                <RoleSelectionModal
                    show={showRoleModal}
                    onSelect={handleRoleSelect}
                    onClose={() => setShowRoleModal(false)}
                />
            )}
        </div>
    );
};

export default Login;

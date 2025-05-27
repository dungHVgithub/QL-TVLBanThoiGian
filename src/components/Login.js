import { useContext, useState } from "react";
import { Button, FloatingLabel, Form, Alert } from "react-bootstrap";
import Api, { authApis, endpoints } from "../configs/Api";
import { MyDispatchContext } from "../configs/MyContexts";
import { useNavigate } from "react-router-dom";
import MySpinner from "./layouts/MySpinner";
import { auth, googleProvider, facebookProvider } from "../configs/FirebaseConfig";
import { signInWithPopup } from "firebase/auth";
import { FaFacebook, FaGoogle, FaSignInAlt } from "react-icons/fa";
import cookie from "react-cookies";
import "../static/login.css"; // Import CSS tùy chỉnh

const Login = () => {
    const info = [
        { label: "👤 Tên đăng nhập", type: "text", field: "username" },
        { label: "🔒 Mật khẩu", type: "password", field: "password" }
    ];

    const dispatch = useContext(MyDispatchContext);
    const nav = useNavigate();

    const [user, setUser] = useState({ username: "", password: "" });
    const [loading, setLoading] = useState(false);
    const [msg, setMsg] = useState("");

    const validateUsername = (username) => {
        const usernameRegex = /^[a-zA-Z0-9_]+$/;
        return usernameRegex.test(username);
    };

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
            setMsg(""); // Xóa thông báo cũ
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
                    id: userInfo.id,
                },
            });

            setMsg("✅ Đăng nhập thành công!");
            setTimeout(() => {
                if (userInfo.role === "ROLE_EMPLOYEE" || userInfo.role === "ROLE_ADMIN")
                    nav("/");
                else if (userInfo.role === "ROLE_EMPLOYER")
                    nav("/employer");
                else nav("/");
            }, 2000); // Đợi 2 giây trước khi điều hướng

        } catch (err) {
            console.error("Lỗi đăng nhập:", err);
            setMsg("❌ Đăng nhập thất bại! Vui lòng kiểm tra lại tên đăng nhập hoặc mật khẩu.");
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

            const payload = {
                name: firebaseUser.displayName,
                email,
                avatar: firebaseUser.photoURL,
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

            setMsg("✅ Đăng nhập bằng mạng xã hội thành công!");
            setTimeout(() => {
                nav("/");
            }, 2000); // Đợi 2 giây trước khi điều hướng

        } catch (err) {
            console.error("OAuth login error:", err);
            if (err.code === "auth/account-exists-with-different-credential") {
                setMsg("⚠️ Tài khoản đã được đăng nhập bằng Google. Vui lòng đăng nhập bằng Google thay vì Facebook.");
            } else if (err.code === "auth/popup-closed-by-user") {
                setMsg("⚠️ Bạn đã đóng cửa sổ đăng nhập quá sớm.");
            } else {
                setMsg("❌ Đăng nhập mạng xã hội thất bại!");
            }
        } finally {
            setLoading(false);
        }
    };

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

                    {loading ? (
                        <div className="login-spinner-container">
                            <MySpinner />
                        </div>
                    ) : (
                        <div className="login-buttons-container">
                            <Button 
                                type="submit" 
                                className="login-btn login-btn-primary"
                                disabled={loading}
                            >
                                <FaSignInAlt />
                                Đăng nhập
                            </Button>
                            
                            <Button 
                                className="login-btn login-btn-facebook"
                                onClick={() => loginWithProvider(facebookProvider)}
                                disabled={loading}
                            >
                                <FaFacebook />
                                Facebook
                            </Button>
                            
                            <Button 
                                className="login-btn login-btn-google"
                                onClick={() => loginWithProvider(googleProvider)}
                                disabled={loading}
                            >
                                <FaGoogle />
                                Google
                            </Button>
                        </div>
                    )}
                </Form>
            </div>
        </div>
    );
};

export default Login;
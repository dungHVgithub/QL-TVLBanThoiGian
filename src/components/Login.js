import { useContext, useState } from "react";
import { Button, FloatingLabel, Form } from "react-bootstrap";
import Api, { authApis, endpoints } from "../configs/Api";
import { MyDispatchContext } from "../configs/MyContexts";
import { useNavigate } from "react-router-dom";
import MySpinner from "./layouts/MySpinner";
import { auth, googleProvider, facebookProvider } from "../configs/FirebaseConfig";
import { signInWithPopup } from "firebase/auth";
import { FaFacebook, FaGoogle } from "react-icons/fa";
import cookie from "react-cookies";
import RoleSelectionModal from "./RoleSelectionModal.js";

const Login = () => {
    const info = [
        { label: "Tên đăng nhập", type: "text", field: "username" },
        { label: "Mật khẩu", type: "password", field: "password" }
    ];

    const dispatch = useContext(MyDispatchContext);
    const nav = useNavigate();

    const [user, setUser] = useState({ username: "", password: "" });
    const [loading, setLoading] = useState(false);
    const [, setMsg] = useState("");
    const [showRoleModal, setShowRoleModal] = useState(false);
    const [googleUser, setGoogleUser] = useState(null);
    const [, setEmailExists] = useState(undefined);

    const login = async (e) => {
        e.preventDefault();
        try {
            setLoading(true);
            const res = await Api.post(endpoints.login, user);
            const token = res.data.token;

            cookie.save("token", token);

            localStorage.setItem("token",token);

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
            console.error("❌ Lỗi đăng nhập:", err);
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

            const result = await signInWithPopup(auth, provider);
            const firebaseUser = result.user;

            let email = firebaseUser.email;
            if (!email && provider === facebookProvider) {
                email = prompt("Facebook không cung cấp email. Nhập email để tiếp tục:");
                if (!email) throw new Error("Email bắt buộc!");
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

                if (userInfo.role === "ROLE_EMPLOYEE" || userInfo.role === "ROLE_ADMIN")
                    nav("/");
                else if (userInfo.role === "ROLE_EMPLOYER")
                    nav("/employer");
                else
                    nav("/");
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

            setShowRoleModal(false);

            if (userInfo.role === "ROLE_EMPLOYEE" || userInfo.role === "ROLE_ADMIN")
                nav("/");
            else if (userInfo.role === "ROLE_EMPLOYER")
                nav("/employer");
            else
                nav("/");
        } catch (err) {
            console.error("❌ Xử lý vai trò thất bại:", err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <h1 className="text-center text-success mt-1">ĐĂNG NHẬP NGƯỜI DÙNG</h1>

            <div className="text-center mb-3">
                <Button variant="primary" className="me-2" onClick={() => loginWithProvider(facebookProvider)}>
                    <FaFacebook className="me-2" /> Facebook
                </Button>
                <Button variant="danger" onClick={() => loginWithProvider(googleProvider)}>
                    <FaGoogle className="me-2" /> Google
                </Button>
            </div>

            <Form onSubmit={login}>
                {info.map((f) => (
                    <FloatingLabel
                        key={f.field}
                        controlId={`floating-${f.field}`}
                        label={f.label}
                        className="mb-3"
                    >
                        <Form.Control
                            type={f.type}
                            placeholder={f.label}
                            value={user[f.field] || ""}
                            onChange={(e) => setState(e.target.value, f.field)}
                            required
                        />
                    </FloatingLabel>
                ))}

                {loading ? (
                    <MySpinner />
                ) : (
                    <Button type="submit" variant="success" className="mt-1 mb-1">
                        Đăng nhập
                    </Button>
                )}
            </Form>

            <RoleSelectionModal
                show={showRoleModal}
                onSelect={handleRoleSelect}
                onClose={() => setShowRoleModal(false)}
            />
        </>
    );
};

export default Login;

import { useContext, useState } from "react";
import { Button, FloatingLabel, Form } from "react-bootstrap";
import Api, { endpoints } from "../configs/Api";
import cookie from "react-cookies";
import { MyDispatchContext } from "../configs/MyContexts";
import { useNavigate } from "react-router-dom";
import MySpinner from "./layouts/MySpinner";

const Login = () => {
    const info = [
        { label: "Tên đăng nhập", type: "text", field: "username" },
        { label: "Mật khẩu", type: "password", field: "password" }
    ];

    const dispatch = useContext(MyDispatchContext);
    const nav = useNavigate();

 
    const [user, setUser] = useState({
        username: "",
        password: ""
    });

    const [loading, setLoading] = useState(false);

    const login = async (e) => {
        e.preventDefault();

        try {
            setLoading(true);

            
            let res = await Api.post(endpoints['login'], user);
            console.info("Token nhận được:", res.data);

            
            cookie.save('token', res.data.token);

            
            dispatch({
                type: "login",
                payload: { username: user.username }
            });

            nav("/");
        } catch (err) {
            console.error("Lỗi đăng nhập:", err);
        } finally {
            setLoading(false);
        }
    };

    const setState = (value, field) => {
        setUser({ ...user, [field]: value });
    };

    return (
        <>
            <h1 className="text-center text-success mt-1">ĐĂNG NHẬP NGƯỜI DÙNG</h1>
            <Form onSubmit={login}>
                {info.map(f => (
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
                            onChange={e => setState(e.target.value, f.field)}
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
        </>
    );
};

export default Login;

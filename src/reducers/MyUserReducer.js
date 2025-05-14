

const MyUserReducer = (current, action) => {
    switch (action.type) {
        case "login":
            localStorage.setItem("token", action.payload.token);  // Lưu token
            return action.payload;

        case "logout":
            localStorage.removeItem("token"); // Xoá token khi logout
            return null;

        default:
            return current;
    }
};

export default MyUserReducer;
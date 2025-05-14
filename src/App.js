import { BrowserRouter, Route,Routes } from "react-router-dom";
import Header from "./components/layouts/Header";
import Footer from "./components/layouts/Footer";
import Home from  "./components/Home";
import 'bootstrap/dist/css/bootstrap.min.css';
import { MyUserContext } from "./configs/MyContexts";
import MyUserReducer from "./reducers/MyUserReducer";
import { Container } from "react-bootstrap";
import { useReducer } from "react";
import { MyDispatchContext } from "./configs/MyContexts";
import Login from "./components/Login";
import Register from "./components/Register";
const App =() => {
  const [user, dispatch] = useReducer(MyUserReducer, null);
  return (
<MyUserContext.Provider value={user}>
      <MyDispatchContext.Provider value={dispatch}>
            <BrowserRouter>
              <Header />      
              <Container>
                <Routes>
                  <Route path="/" element={<Home />} />
                  <Route path="/register" element={<Register />} />
                  <Route path="/login" element={<Login />} />
                </Routes>
              </Container>
              <Footer />
            </BrowserRouter>
      </MyDispatchContext.Provider>
    </MyUserContext.Provider>
);  
}
export default App;
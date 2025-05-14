import ou from "../../img/ou.png";
import '../../static/footer.css';

const Footer = () => {
    return (
        <footer className="footer-container">
            <div className="footer-content">
                <div className="footer-logo-section">
                    <img src={ou} alt="Find Job Logo" className="footer-logo" />
                    <h1 className="footer-title">Công Ty TNHH Dung&Khang Technology  </h1>
                </div>
                <div className="footer-info">
                    <p><strong>Địa chỉ:</strong> 123 Nguyễn Bình, Huyện Nhà Bè, TP. Hồ Chí Minh</p>
                    <p><strong>Số điện thoại:</strong> (028)99999999</p>
                    <p><strong>Email:</strong> contact@findjob.com</p>
                </div>
            </div>
            <div className="footer-bottom">
                <p>&copy; 2025 Find Job. All rights reserved.</p>
            </div>
        </footer>
    );
};

export default Footer;
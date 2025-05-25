import React from "react";
import { Modal, Button } from "react-bootstrap";
import { FaUserTie, FaUser } from "react-icons/fa";

const RoleSelectionModal = ({ show, onSelect, onClose }) => {
  return (
    <Modal show={show} onHide={onClose} centered backdrop="static">
      <Modal.Header closeButton>
        <Modal.Title>Chọn vai trò của bạn</Modal.Title>
      </Modal.Header>
      <Modal.Body className="text-center">
        <p className="mb-4">Bạn muốn tiếp tục với tư cách là:</p>
        <div className="d-flex justify-content-around">
          <Button variant="outline-primary" onClick={() => onSelect("ROLE_EMPLOYEE")}>
            <FaUser className="me-2" /> Nhân viên
          </Button>
          <Button variant="outline-success" onClick={() => onSelect("ROLE_EMPLOYER")}>
            <FaUserTie className="me-2" /> Nhà tuyển dụng
          </Button>
        </div>
      </Modal.Body>
    </Modal>
  );
};

export default RoleSelectionModal;

import React from "react";
import { Modal, Button, Form } from "react-bootstrap";

const SelectRoleModal = ({ show, onClose, onSelect }) => {
    const [selectedRole, setSelectedRole] = React.useState("ROLE_EMPLOYEE");

    const handleConfirm = () => {
        onSelect(selectedRole);
        onClose();
    };

    return (
        <Modal show={show} onHide={onClose} centered backdrop="static">
            <Modal.Header closeButton>
                <Modal.Title>Chọn quyền đăng ký</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Check
                        type="radio"
                        label="👨‍💼 Nhân viên (Tìm việc)"
                        id="modal-role-employee"
                        name="modal-role"
                        value="ROLE_EMPLOYEE"
                        checked={selectedRole === "ROLE_EMPLOYEE"}
                        onChange={(e) => setSelectedRole(e.target.value)}
                    />
                    <Form.Check
                        type="radio"
                        id="modal-role-employer"
                        label="🏢 Nhà tuyển dụng(Đăng tin)"
                        name="modal-role"
                        value="ROLE_EMPLOYER"
                        checked={selectedRole === "ROLE_EMPLOYER"}
                        onChange={(e) => setSelectedRole(e.target.value)}
                    />
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onClose}>
                    Hủy
                </Button>
                <Button variant="primary" onClick={handleConfirm}>
                    Tiếp tục
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default SelectRoleModal;

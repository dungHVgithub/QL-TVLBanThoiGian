/*
 * Click nbfs://.netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://.netbeans/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.services.impl;

import com.job.pojo.UserDocuments;
import com.job.repositories.UserDocRepository;
import com.job.services.UserDocService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author DUNG
 */
@Service
public class UserDocServiceImpl implements UserDocService {

    @Autowired
    private UserDocRepository userDocRepo;

    @Override
    public List<UserDocuments> getUserDocs(Map<String, String> params) {
        return this.userDocRepo.getUserDocs(params);
    }

    @Override
    public UserDocuments getUserDocsById(int id) {
        return this.userDocRepo.getUserDocsById(id);
    }

    @Override
    public UserDocuments addOrUpdate(UserDocuments j) {
        return this.userDocRepo.addOrUpdate(j);
    }

    @Override
    public void deleteUserDocs(int id) {
        this.userDocRepo.deleteUserDocs(id);
    }

    @Override
    public List<UserDocuments> getDocsByUserId(int userId) {
        return this.userDocRepo.getDocsByUserId(userId);
    }

    // Thêm phương thức để lấy danh sách tài liệu theo employeeId
    public List<UserDocuments> getDocsByEmployeeId(int employeeId) {
        return this.userDocRepo.getDocsByEmployeeId(employeeId);
    }

    // Thêm hoặc cập nhật tài liệu cho employeeId cụ thể
    public UserDocuments addOrUpdateForEmployee(UserDocuments doc, int employeeId) {
        return this.userDocRepo.addOrUpdateForEmployee(doc, employeeId);
    }
}
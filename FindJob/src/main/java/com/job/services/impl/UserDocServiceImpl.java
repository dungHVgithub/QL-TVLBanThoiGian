/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
    private UserDocRepository UserDocRepo;

    @Override
    public List<UserDocuments> getUserDocs(Map<String, String> params) {
        return this.UserDocRepo.getUserDocs(params);
    }

    @Override
    public UserDocuments getUserDocsById(int id) {
        return this.UserDocRepo.getUserDocsById(id);
    }

    @Override
    public UserDocuments addOrUpdate(UserDocuments j) {
        return this.UserDocRepo.addOrUpdate(j);

    }

    @Override
    public void deleteUserDocs(int id) {
        this.UserDocRepo.deleteUserDocs(id);
    }

    @Override
    public List<UserDocuments> getDocsByUserId(int userId) {
        return this.UserDocRepo.getDocsByUserId(userId);
    }

}

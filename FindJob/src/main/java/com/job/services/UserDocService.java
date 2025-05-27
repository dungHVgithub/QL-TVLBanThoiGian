/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.job.services;

import com.job.pojo.UserDocuments;

import java.util.List;
import java.util.Map;

/**
 *
 * @author DUNG
 */
public interface UserDocService {

    List<UserDocuments> getUserDocs(Map<String, String> params);

    UserDocuments getUserDocsById(int id);

    UserDocuments addOrUpdate(UserDocuments j);

    void deleteUserDocs(int id);

    List<UserDocuments> getDocsByUserId(int userId);

    List<UserDocuments> getDocsByEmployeeId(int employeeId);

    UserDocuments addOrUpdateForEmployee(UserDocuments existing, int employeeId);
}

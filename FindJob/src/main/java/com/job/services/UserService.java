/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.job.services;

import java.util.List;
import java.util.Map;

import com.job.pojo.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author DUNG
 */
public interface UserService extends UserDetailsService {

    User getUserByUserName(String username);
    User addUser(Map<String, String> params, MultipartFile avatar);
    User addUpdateUser(User u);
    List <User> getUser(Map <String , String> params);
    User getUserById(int id);
    void deleteUser(int id);
    boolean authenticate(String username, String password);
    User getUserByEmail(String email);
    long countEmployees(); //Method dem employeee

    List<User> getUsersByRole(String role);
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.job.repositories;

import com.job.pojo.User;

import java.util.List;
import java.util.Map;

/**
 *
 * @author DUNG
 */
public interface UserRepository {

    User getUserByUserName(String username);

    User addUpdateUser(User u);

    List<User> getUser(Map<String, String> params);

    User getUserById(int id);

    void deleteUser(int id);

    boolean authenticate(String username, String password);

    User getUserByEmail(String email);
    long countByRole(String role); //Thong ke user theo vai tro
    long countByRoleAndMonth(String role, String month);
    long countByRoleAndDate(String role, String date); // Thêm phương thức mới
    List<User> getUsersByRole(String role);

}

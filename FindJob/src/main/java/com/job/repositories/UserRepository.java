/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.job.repositories;

import com.job.pojo.User;

/**
 *
 * @author DUNG
 */
public interface UserRepository {
    User getUserByUserName(String username);
}

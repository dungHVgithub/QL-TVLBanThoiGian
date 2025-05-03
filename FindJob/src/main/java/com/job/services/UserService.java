/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.job.services;

import com.job.pojo.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 *
 * @author DUNG
 */
public interface UserService extends UserDetailsService{
     User getUserByUserName(String username);
}

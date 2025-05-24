/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.job.pojo.User;
import com.job.repositories.UserRepository;
import com.job.services.UserService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author DUNG
 */
@Service("userDetailsService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public User getUserByUserName(String username) {
        return this.userRepo.getUserByUserName(username);
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User u = this.getUserByUserName(usernameOrEmail);

      
        if (u == null) {
            u = this.getUserByEmail(usernameOrEmail);
        }

        if (u == null) {
            throw new UsernameNotFoundException("Không tìm thấy người dùng");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(u.getRole()));

        return new org.springframework.security.core.userdetails.User(
                u.getUsername() != null ? u.getUsername() : u.getEmail(),
                u.getPassword() != null ? u.getPassword() : "", // OAuth có thể không có password
                authorities
        );
    }

    @Override
    public User addUser(Map<String, String> params, MultipartFile avatar) {
        User u = new User();
        u.setName(params.get("name"));
        u.setAddress(params.get("address"));
        u.setEmail(params.get("email"));
        u.setSdt(params.get("sdt"));
        u.setUsername(params.get("username"));
        u.setPassword(this.passwordEncoder.encode(params.get("password")));
        u.setRole(params.get("role"));

        if (!avatar.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                u.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(JobPostingServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return this.userRepo.addUpdateUser(u);
    }

    @Override
    public boolean authenticate(String username, String password) {
        return this.userRepo.authenticate(username, password);
    }

    @Override
    public List<User> getUser(Map<String, String> params) {
        return this.userRepo.getUser(params);
    }

    @Override
    public User getUserById(int id) {
        return this.userRepo.getUserById(id);
    }

    @Override
    public User addUpdateUser(User u) {

        if (u.getPassword() != null && !u.getPassword().isEmpty()) {
            u.setPassword(this.passwordEncoder.encode(u.getPassword()));
        }

        if (u.getFile() != null && !u.getFile().isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(u.getFile().getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                u.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Xử lý createdAt và updatedAt
        LocalDateTime now = LocalDateTime.now();
        // Chuyển đổi LocalDateTime sang Date, sử dụng múi giờ Asia/Ho_Chi_Minh (+07:00)
        Date currentDate = Date.from(now.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant());

        if (u.getId() == null) {
            // Khi add mới: đặt cả createdAt và updatedAt giống nhau
            u.setCreatedAt(currentDate);
            u.setUpdatedAt(currentDate);
        } else {
            // Khi update: chỉ cập nhật updatedAt
            u.setUpdatedAt(currentDate);
            // createdAt giữ nguyên, không cần set lại
        }

        return this.userRepo.addUpdateUser(u);
    }

    @Override
    public void deleteUser(int id) {
        this.userRepo.deleteUser(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return this.userRepo.getUserByEmail(email);
    }

    @Override
    public long countEmployees() {
        return this.userRepo.countByRole("ROLE_EMPLOYEE");
    }

    @Override
    public long countEmployer() {
        return this.userRepo.countByRole("ROLE_EMPLOYER");
    }

    @Override
    public List<User> getUsersByRole(String role) {
        return this.userRepo.getUsersByRole(role);
    }

}

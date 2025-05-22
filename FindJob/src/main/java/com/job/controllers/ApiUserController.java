package com.job.controllers;

import com.job.pojo.User;
import com.job.services.UserService;
import com.job.utils.JwtUtils;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiUserController {

    @Autowired
    private UserService userDetailsService;

    @PostMapping(path = "/users",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@RequestParam Map<String, String> params, @RequestParam(value = "avatar") MultipartFile avatar) {
        return new ResponseEntity<>(this.userDetailsService.addUser(params, avatar), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User u) {

        if (this.userDetailsService.authenticate(u.getUsername(), u.getPassword())) {
            try {
                String token = JwtUtils.generateToken(u.getUsername());
                return ResponseEntity.ok().body(Collections.singletonMap("token", token));
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Lỗi khi tạo JWT");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai thông tin đăng nhập");
    }

    @RequestMapping("/secure/profile")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<?> getProfile(Principal user) {
        String principalName = user.getName();

        User u = userDetailsService.getUserByUserName(principalName);
        if (u == null) {
            u = userDetailsService.getUserByEmail(principalName);
        }

        if (u == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng!");
        }

        return ResponseEntity.ok(u);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "userId") int id) {
        this.userDetailsService.deleteUser(id);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> list(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.userDetailsService.getUser(params), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<User> retrieve(@PathVariable(value = "userId") int id) {
        return new ResponseEntity<>(this.userDetailsService.getUserById(id), HttpStatus.OK);
    }

    @PostMapping("/oauth-login")
    public ResponseEntity<?> oauthLogin(@RequestBody Map<String, String> payload) {
        try {
            String email = payload.get("email");
            String name = payload.get("name");
            String avatar = payload.get("avatar");

            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email là bắt buộc!");
            }

            User user = userDetailsService.getUserByEmail(email);

            if (user == null) {
                // Tạo user mới
                user = new User();
                user.setEmail(email);
                user.setName(name);
                user.setAvatar(avatar);
                user.setRole("ROLE_EMPLOYEE");
                user = userDetailsService.addUpdateUser(user);
            }

            String token = JwtUtils.generateToken(email);
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (Exception ex) {
            ex.printStackTrace(); // Log chi tiết để dễ debug
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi đăng nhập OAuth");
        }
    }
    @GetMapping("/users/count/employees")
    public ResponseEntity<Map<String, Long>> countEmployees() {
        long count = userDetailsService.countEmployees();
        return ResponseEntity.ok(Collections.singletonMap("totalEmployees", count));
    }
}

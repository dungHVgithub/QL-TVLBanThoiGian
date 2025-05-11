package com.job.controllers;

import com.job.pojo.User;
import com.job.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Arrays;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userDetailsService;

    @GetMapping("/login")
    public String loginView() {
        return "login";
    }

    @GetMapping("/users")
    public String userList(Model model, @RequestParam(required = false) Map<String, String> params) {
        model.addAttribute("users", userDetailsService.getUser(params));
        return "user";
    }

    @GetMapping("/users/add")
    public String addUserView(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Arrays.asList("ROLE_EMPLOYEE", "ROLE_ADMIN", "ROLE_EMPLOYER")); // Danh sách vai trò tĩnh
        return "editUser";
    }

    @GetMapping("/users/{userId}")
    public String updateUserView(Model model, @PathVariable(value = "userId") int id) {
        model.addAttribute("user", this.userDetailsService.getUserById(id));
        model.addAttribute("roles", Arrays.asList("ROLE_EMPLOYEE", "ROLE_ADMIN", "ROLE_EMPLOYER")); // Danh sách vai trò tĩnh
        return "editUser";
    }
    
    @PostMapping("/users/add")
    public String addUser(@ModelAttribute (value = "User") User u)
    {
     this.userDetailsService.addUpdateUser(u);
     return "direct:/";
    }
}
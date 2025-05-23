/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.controllers;

import com.job.pojo.User;
import com.job.services.CategoryService;
import com.job.services.EmployerService;
import com.job.services.JobPostingService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.job.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author DUNG
 */
@Controller
@ControllerAdvice
public class IndexController {

    @Autowired
    private JobPostingService jobService;
    @Autowired
    private CategoryService cateService;

    @Autowired
    private EmployerService employerService;
    @Autowired
    private UserService userService;

    @ModelAttribute
    public void commonRespone(Model model) {
        model.addAttribute("categories", this.cateService.getCates());
    }

        @ModelAttribute
    public void responeEmployer(Model model){
        model.addAttribute("employers",this.employerService.getEmployers());
    }
    @ModelAttribute
    public void userList(Model model, @RequestParam(required = false) Map<String, String> params) {
        model.addAttribute("users", userService.getUser(params));
    }
    @RequestMapping("/")
    public String indexProduct(Model model, @RequestParam Map<String, String> params) {
        System.out.println("Accessing index page with params: " + params);
        model.addAttribute("job_postings", this.jobService.getJobPostings(params));
        return "index";
    }
}

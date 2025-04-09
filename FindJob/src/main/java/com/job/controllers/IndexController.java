/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.controllers;

import com.job.services.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author DUNG
 */
@Controller
public class IndexController {
    @Autowired
    private JobPostingService jobService;
    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("JobPosting",this.jobService.getJobPostings());
        return "index";
    }
}

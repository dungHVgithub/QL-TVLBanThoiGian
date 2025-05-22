    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.controllers;
import com.job.pojo.JobPosting;
import com.job.pojo.User;
import com.job.services.EmployerService;
import com.job.services.JobPostingService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.job.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 *
 * @author AN515-57
 */
@Controller
public class jobPostingController {
    @Autowired
    private UserService userService;
    @Autowired
    private JobPostingService jobService;

    @Autowired
    private EmployerService employerService;

    @GetMapping("/job_postings")
    public String addView(Model model, @RequestParam Map<String, String> params) {
        List<User> employers = userService.getUsersByRole("ROLE_EMPLOYER");
        model.addAttribute("job_posting", new JobPosting());
        model.addAttribute("job_postings", jobService.getJobPostings(params));
        model.addAttribute("states", Arrays.asList("approved", "rejected", "pending")); // Danh sách tĩnh
        return "job_postings";
    }

    @GetMapping("/job_postings/{jobPostingId}")
    public String updateView(Model model, @PathVariable (value = "jobPostingId") int id)
    {
        model.addAttribute("job_posting", this.jobService.getJobById(id));
        model.addAttribute("states", Arrays.asList("approved", "rejected", "pending")); // Danh sách tĩnh
        return "job_postings";
    }

    @PostMapping("/add")
    public String add (@ModelAttribute (value = "jobPosting") JobPosting j){
      this.jobService.addOrUpdate(j);
      return "redirect:/";
    }
 }

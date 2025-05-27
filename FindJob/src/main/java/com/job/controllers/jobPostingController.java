/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nfs://SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.controllers;

import com.job.pojo.JobPosting;
import com.job.pojo.Notification;
import com.job.pojo.User;
import com.job.services.EmployerService;
import com.job.services.JobPostingService;
import com.job.services.NotificationService;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.job.services.UserService;
import java.util.Date;
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

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/job_postings")
    public String addView(Model model, @RequestParam Map<String, String> params) {
        List<User> employers = userService.getUsersByRole("ROLE_EMPLOYER");
        model.addAttribute("job_posting", new JobPosting());
        model.addAttribute("job_postings", jobService.getJobPostings(params));
        model.addAttribute("states", Arrays.asList("approved", "rejected", "pending"));
        return "job_postings";
    }

    @GetMapping("/job_postings/{jobPostingId}")
    public String updateView(Model model, @PathVariable(value = "jobPostingId") int id) {
        model.addAttribute("job_posting", this.jobService.getJobById(id));
        model.addAttribute("states", Arrays.asList("approved", "rejected", "pending"));
        return "job_postings";
    }

    @PostMapping("/add")

    public String add(@ModelAttribute(value = "jobPosting") JobPosting j) {
        JobPosting old = this.jobService.getJobById(j.getId());
        this.jobService.addOrUpdate(j);

        if (old != null && "pending".equalsIgnoreCase(old.getState()) && "approved".equalsIgnoreCase(j.getState())) {
            Notification noti = new Notification();
            noti.setContent("Nhà tuyển dụng vừa được duyệt một bài viết: " + j.getName());
            noti.setEmployerId(j.getEmployerId());
            noti.setCreatedAt(new Date());
            this.notificationService.addUserNotification(noti);
        }

        return "redirect:/";
    }
}


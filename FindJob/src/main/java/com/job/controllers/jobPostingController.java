/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.controllers;
import com.job.services.JobPostingService;
import com.job.pojo.JobPosting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


/**
 *
 * @author AN515-57
 */
@Controller
public class jobPostingController {
    @Autowired
    private JobPostingService jobService;
    
       @GetMapping("/job_postings")
    public String addView(Model model) {
        model.addAttribute("job_posting", new JobPosting());
        return "job_postings";
    }
    
    @GetMapping("/job_postings/{jobPostingId}")
    public String updateView(Model model, @PathVariable (value = "jobPostingId") int id)
    {
        model.addAttribute("job_posting", this.jobService.getJobById(id));
        return "job_postings";
    }
    
    @PostMapping("/add")
    public String add (@ModelAttribute (value = "jobPosting") JobPosting j){
      this.jobService.addOrUpdate(j);
      return "redirect:/";
    }
}

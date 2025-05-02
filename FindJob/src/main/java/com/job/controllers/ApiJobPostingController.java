/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.controllers;

/**
 *
 * @author AN515-57
 */
import com.job.services.JobPostingService;
import com.job.pojo.JobPosting;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiJobPostingController {
    @Autowired
    private JobPostingService jobService;
    
    @DeleteMapping("/job_postings/{jobPostingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable (value = "jobPostingId") int id)
    {
        this.jobService.deleteJob(id);
    }
    
    @GetMapping("/products")
    public ResponseEntity<List<JobPosting>> list(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.jobService.getJobPostings(params), HttpStatus.OK);
    } 
    
    @GetMapping("/job_postings/{jobPostingId}")
    public ResponseEntity<JobPosting> retrieve(@PathVariable(value = "jobPostingId") int id) {
        return new ResponseEntity<>(this.jobService.getJobById(id), HttpStatus.OK);
    }
    
}

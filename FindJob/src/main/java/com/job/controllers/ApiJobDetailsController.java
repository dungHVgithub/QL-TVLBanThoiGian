package com.job.controllers;

import com.job.pojo.JobDescription;
import com.job.services.JobDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/job-details")
public class ApiJobDetailsController {

    @Autowired
    private JobDetailService jobDetailService;

    @GetMapping
    public ResponseEntity<List<JobDescription>> getJobDetails(@RequestParam Map<String, String> params) {
        List<JobDescription> jobDetails = jobDetailService.getJobDetail(params);
        return ResponseEntity.ok(jobDetails);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDescription> getJobDetailById(@PathVariable("id") int id) {
        JobDescription jobDetail = jobDetailService.getJobDetailById(id);
        return ResponseEntity.ok(jobDetail);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable("id") int id)
    {
        this.jobDetailService.deleteJobDetail(id);
    }
}
/*
 * Click nfs://SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nfs://SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.controllers;

import com.job.dto.JobPostingDTO;
import com.job.pojo.Category;
import com.job.pojo.Employer;
import com.job.pojo.JobPosting;
import com.job.services.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @author AN515-57
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ApiJobPostingController {
    @Autowired
    private JobPostingService jobService;

    @PostMapping("/job_postings")
    public ResponseEntity<JobPosting> createJobPosting(@RequestBody JobPostingDTO dto) {
        try {
            JobPosting jobPosting = new JobPosting();
            jobPosting.setName(dto.getName());
            jobPosting.setSalary(dto.getSalary());
            jobPosting.setState(dto.getState() != null ? dto.getState() : "pending");

            // Chuyển đổi timeStart và timeEnd từ chuỗi "HH:mm" sang Date
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            if (dto.getTimeStart() != null) {
                jobPosting.setTimeStart(timeFormat.parse(dto.getTimeStart()));
            }
            if (dto.getTimeEnd() != null) {
                jobPosting.setTimeEnd(timeFormat.parse(dto.getTimeEnd()));
            }

            // Thiết lập categoryId và employerId
            if (dto.getCategoryId() != null) {
                Category category = new Category();
                category.setId(dto.getCategoryId());
                jobPosting.setCategoryId(category);
            }
            if (dto.getEmployerId() != null) {
                Employer employer = new Employer();
                employer.setId(dto.getEmployerId());
                jobPosting.setEmployerId(employer);
            }

            JobPosting savedJob = jobService.addOrUpdate(jobPosting);
            return new ResponseEntity<>(savedJob, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/job_postings/{jobPostingId}")
    public ResponseEntity<JobPosting> updateJobPosting(@PathVariable("jobPostingId") int id, @RequestBody JobPostingDTO dto) {
        try {
            JobPosting existingJob = jobService.getJobById(id);
            if (existingJob == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Cập nhật các trường
            existingJob.setName(dto.getName() != null ? dto.getName() : existingJob.getName());
            existingJob.setSalary(dto.getSalary() != null ? dto.getSalary() : existingJob.getSalary());
            existingJob.setState(dto.getState() != null ? dto.getState() : existingJob.getState());

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            if (dto.getTimeStart() != null) {
                existingJob.setTimeStart(timeFormat.parse(dto.getTimeStart()));
            }
            if (dto.getTimeEnd() != null) {
                existingJob.setTimeEnd(timeFormat.parse(dto.getTimeEnd()));
            }

            if (dto.getCategoryId() != null) {
                Category category = new Category();
                category.setId(dto.getCategoryId());
                existingJob.setCategoryId(category);
            }
            if (dto.getEmployerId() != null) {
                Employer employer = new Employer();
                employer.setId(dto.getEmployerId());
                existingJob.setEmployerId(employer);
            }

            JobPosting updatedJob = jobService.addOrUpdate(existingJob);
            return new ResponseEntity<>(updatedJob, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/job_postings/{jobPostingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "jobPostingId") int id) {
        this.jobService.deleteJob(id);
    }

    @GetMapping("/job_postings")
    public ResponseEntity<List<JobPosting>> list(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.jobService.getJobPostings(params), HttpStatus.OK);
    }

    @GetMapping("/job_postings/{jobPostingId}")
    public ResponseEntity<JobPosting> retrieve(@PathVariable(value = "jobPostingId") int id) {
        JobPosting job = this.jobService.getJobById(id);
        if (job != null) {
            return new ResponseEntity<>(job, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/job_postings/count")
    public ResponseEntity<Map<String, Long>> countJobs() {
        long count = jobService.countJobs();
        return ResponseEntity.ok(Collections.singletonMap("totalJobs", count));
    }

    @GetMapping("/job_postings/count/{state}")
    public ResponseEntity<Map<String, Object>> countJobsByState(@PathVariable("state") String state) {
        String normalizedState = state.toUpperCase();
        if (!normalizedState.equals("PENDING") && !normalizedState.equals("APPROVED") && !normalizedState.equals("REJECTED")) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Invalid state"));
        }
        long count = jobService.countJobsByState(normalizedState);
        return ResponseEntity.ok(Collections.singletonMap("totalJobsByState", count));
    }

    @GetMapping("/job_postings/employer/{employerId}")
    public ResponseEntity<List<JobPosting>> getJobsByEmployer(@PathVariable("employerId") int employerId) {
        List<JobPosting> jobs = jobService.getJobPostingsByEmployerId(employerId);
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }
}
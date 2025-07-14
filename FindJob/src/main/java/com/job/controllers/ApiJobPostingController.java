/*
 * Click nfs://SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nfs://SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.controllers;

import com.job.dto.JobPostingDTO;
import com.job.pojo.Category;
import com.job.pojo.Employer;
import com.job.pojo.JobPosting;
import com.job.pojo.Notification;
import com.job.services.JobPostingService;
import com.job.services.NotificationService;
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
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/job_postings")
    public ResponseEntity<JobPostingDTO> createJobPosting(@RequestBody JobPostingDTO dto) {
        try {
            JobPosting jobPosting = new JobPosting();
            jobPosting.setId(dto.getId());
            jobPosting.setName(dto.getName());
            jobPosting.setSalary(dto.getSalary());
            jobPosting.setState(dto.getState() != null ? dto.getState() : "pending");

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            if (dto.getTimeStart() != null) {
                jobPosting.setTimeStart(timeFormat.parse(dto.getTimeStart()));
            }
            if (dto.getTimeEnd() != null) {
                jobPosting.setTimeEnd(timeFormat.parse(dto.getTimeEnd()));
            }

            if (dto.getCategoryId() != null) {
                Category category = new Category();
                category.setId(dto.getCategoryId());
                jobPosting.setCategoryId(category);
            }

            Employer employer = new Employer();
            if (dto.getEmployer() != null && dto.getEmployer().getId() != null) {
                employer.setId(dto.getEmployer().getId());
            }
            jobPosting.setEmployerId(employer);

            // Save tạm job
            JobPosting savedJob = jobService.addOrUpdate(jobPosting);

            // Chuyển đổi sang DTO để trả về
            JobPostingDTO responseDTO = jobService.getJobByIdAsDTO(savedJob.getId());

            // Đợi admin duyệt mới gửi
            if ("approved".equalsIgnoreCase(savedJob.getState())) {
                Notification notification = new Notification();
                notification.setContent("Nhà tuyển dụng vừa đăng tin tuyển dụng mới: " + savedJob.getName());
                notification.setEmployerId(employer);

                notificationService.addUserNotification(notification);
            }

            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/job_postings/{jobPostingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "jobPostingId") int id) {
        this.jobService.deleteJob(id);
    }

    @GetMapping("/job_postings")
    public ResponseEntity<List<JobPostingDTO>> list(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.jobService.getJobPostingsAsDTO(params), HttpStatus.OK);
    }

    @GetMapping("/job_postings/{jobPostingId}")
    public ResponseEntity<JobPostingDTO> retrieve(@PathVariable(value = "jobPostingId") int id) {
        JobPostingDTO dto = this.jobService.getJobByIdAsDTO(id);
        if (dto != null) {
            return new ResponseEntity<>(dto, HttpStatus.OK);
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
}

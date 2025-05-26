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
@RequestMapping("/api/job_details")
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

    @PostMapping
    public ResponseEntity<JobDescription> createJobDetail(@RequestBody JobDescription jobDetail) {
        try {
            // Kiểm tra jobPosting có id không
            if (jobDetail.getJobPosting() == null || jobDetail.getJobPosting().getId() == null) {
                return ResponseEntity.badRequest().body(null);
            }
            JobDescription savedJobDetail = jobDetailService.saveJobDetail(jobDetail);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedJobDetail);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobDescription> updateJobDetail(@PathVariable("id") int id, @RequestBody JobDescription jobDetail) {
        try {
            JobDescription existingJobDetail = jobDetailService.getJobDetailById(id);
            if (existingJobDetail == null) {
                return ResponseEntity.notFound().build();
            }

            // Cập nhật các trường
            existingJobDetail.setDescription(jobDetail.getDescription());
            existingJobDetail.setLevel(jobDetail.getLevel());
            existingJobDetail.setExperience(jobDetail.getExperience());
            existingJobDetail.setSubmitEnd(jobDetail.getSubmitEnd());
            existingJobDetail.setBenefit(jobDetail.getBenefit());
            if (jobDetail.getJobPosting() != null && jobDetail.getJobPosting().getId() != null) {
                existingJobDetail.setJobPosting(jobDetail.getJobPosting());
            }

            JobDescription updatedJobDetail = jobDetailService.saveJobDetail(existingJobDetail);
            return ResponseEntity.ok(updatedJobDetail);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobDetail(@PathVariable("id") int id) {
        try {
            JobDescription jobDetail = jobDetailService.getJobDetailById(id);
            if (jobDetail == null) {
                return ResponseEntity.notFound().build();
            }
            jobDetailService.deleteJobDetail(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/jobPosting/{jobPostingId}")
    public ResponseEntity<JobDescription> getJobDetailByJobPostingId(@PathVariable("jobPostingId") int jobPostingId) {
        JobDescription jobDetail = jobDetailService.getJobDetailByJobPostingId(jobPostingId);
        return ResponseEntity.ok(jobDetail);
    }
    @PutMapping("/jobPosting/{jobPostingId}")
    public ResponseEntity<JobDescription> updateJobDetailByJobPostingId(@PathVariable("jobPostingId") int jobPostingId, @RequestBody JobDescription jobDetail) {
        try {
            // Tìm JobDescription hiện có dựa trên jobPostingId
            JobDescription existingJobDetail = jobDetailService.getJobDetailByJobPostingId(jobPostingId);
            if (existingJobDetail == null) {
                return ResponseEntity.notFound().build();
            }

            // Cập nhật các trường
            existingJobDetail.setDescription(jobDetail.getDescription());
            existingJobDetail.setLevel(jobDetail.getLevel());
            existingJobDetail.setExperience(jobDetail.getExperience());
            existingJobDetail.setSubmitEnd(jobDetail.getSubmitEnd()); // Đảm bảo submitEnd là Date
            existingJobDetail.setBenefit(jobDetail.getBenefit());
            if (jobDetail.getJobPosting() != null && jobDetail.getJobPosting().getId() != null) {
                existingJobDetail.setJobPosting(jobDetail.getJobPosting());
            }

            // Lưu và trả về
            JobDescription updatedJobDetail = jobDetailService.saveJobDetail(existingJobDetail);
            System.out.println("Updated JobDetail: " + updatedJobDetail); // Log để kiểm tra
            return ResponseEntity.ok(updatedJobDetail);
        } catch (Exception e) {
            System.err.println("Error updating jobDetail: " + e.getMessage()); // Log lỗi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
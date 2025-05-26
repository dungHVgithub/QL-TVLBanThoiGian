package com.job.controllers;

import com.job.pojo.EmployeeJob;
import com.job.services.EmployeeJobService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employeeJob/employee")
public class ApiEmployeeJobController {

    @Autowired
    private EmployeeJobService employeeJobService;

    // GET: Lấy danh sách EmployeeJob theo employeeId
    @GetMapping("/{employeeId}")
    public ResponseEntity<List<EmployeeJob>> getEmployeeJobsByEmployeeId(@PathVariable("employeeId") Integer employeeId) {
        try {
            List<EmployeeJob> employeeJobs = employeeJobService.getEmployeeJobsByEmployeeId(employeeId);
            if (employeeJobs.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(employeeJobs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // POST: Thêm mới EmployeeJob
    @PostMapping("/{employeeId}")
    public ResponseEntity<EmployeeJob> createEmployeeJob(
            @PathVariable("employeeId") Integer employeeId,
            @RequestBody EmployeeJob employeeJob) {
        try {
            if (employeeJob.getEmployeeId() == null || employeeJob.getEmployeeId().getId() != employeeId) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            EmployeeJob created = employeeJobService.addOrUpdate(employeeJob);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // PUT: Cập nhật trạng thái EmployeeJob (jobState hoặc favoriteJob)
    @PutMapping("/{employeeId}/{employeeJobId}")
    public ResponseEntity<EmployeeJob> updateEmployeeJob(
            @PathVariable("employeeId") Integer employeeId,
            @PathVariable("employeeJobId") Integer employeeJobId,
            @RequestBody EmployeeJob employeeJob) {
        try {
            // Kiểm tra xem employeeJob có tồn tại không
            EmployeeJob existingJob = employeeJobService.getEmployeeJobById(employeeJobId);
            if (existingJob == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if (existingJob.getEmployeeId() == null || !existingJob.getEmployeeId().getId().equals(employeeId)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // Cập nhật jobState nếu có
            Short newJobState = employeeJob.getJobState();
            if (newJobState != null && (newJobState == 0 || newJobState == 1)) {
                existingJob.setJobState(newJobState);
            }

            // Cập nhật favoriteJob nếu có
            Short newFavoriteJob = employeeJob.getFavoriteJob();
            if (newFavoriteJob != null && (newFavoriteJob == 0 || newFavoriteJob == 1)) {
                existingJob.setFavoriteJob(newFavoriteJob);
            }

            // Nếu không có thay đổi nào, trả về lỗi
            if (newJobState == null && newFavoriteJob == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            EmployeeJob updated = employeeJobService.addOrUpdate(existingJob);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
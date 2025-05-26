package com.job.controllers;

import com.job.dto.FollowNoticeDTOEmployee;
import com.job.dto.FollowNoticeDTOEmployer;
import com.job.pojo.Employee;
import com.job.pojo.Employer;
import com.job.pojo.FollowNotice;
import com.job.services.FollowNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiFollowNoticeController {

    @Autowired
    private FollowNoticeService followService;

    // người dùng follow 1 nhà tuyển dụng
    @PostMapping("/follows")
    public ResponseEntity<?> follow(@RequestParam("employee") int employeeId,
            @RequestParam("employer") int employerId) {
        try {
            Employee employee = new Employee();
            employee.setId(employeeId);

            Employer employer = new Employer();
            employer.setId(employerId);

            FollowNotice follow = new FollowNotice();
            follow.setEmployee(employee);
            follow.setEmployer(employer);
            follow.setFollowTime(new Date());

            FollowNotice result = followService.addOrUpdateFollow(follow);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Không thể tạo follow"));
        }
    }

    @GetMapping("/follows/employee/{employeeId}")// xem các nhà tuyển dụng mà nhân viên đã follow
    public ResponseEntity<?> getFollows(@PathVariable("employeeId") int employeeId) {
        try {
            List<FollowNoticeDTOEmployee> follows = followService.getFollowDTOsByEmployee(employeeId);
            return ResponseEntity.ok(follows);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
        }
    }
     @GetMapping("/follows/employer/{employerId}")// lấy các follower để gửi thông báo
    public ResponseEntity<?> getFollowers(@PathVariable("employerId") int employerId) {
        try {
            List<FollowNoticeDTOEmployer> follows = followService.getFollowerDTOsByEmployer(employerId);
            return ResponseEntity.ok(follows);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    @GetMapping("/follow-count/{employerId}")// đếm tổng follow của 1 nhà tuyển dụng -> hiện ra số tổng follow trên nút follow
    public ResponseEntity<Map<String, Long>> countFollowers(@PathVariable("employerId") int employerId) {
        long count = followService.countFollowersByEmployer(employerId);
        return ResponseEntity.ok(Collections.singletonMap("count", count));
    }

    @DeleteMapping("/follows/{employeeId}/{employerId}")
    public ResponseEntity<?> deleteFollow(@PathVariable("employeeId") int employeeId,
            @PathVariable("employerId") int employerId) {
        boolean deleted = followService.deleteFollow(employeeId, employerId);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/follows/follow-exists/{employeeId}/{employerId}")
    public ResponseEntity<Map<String, Boolean>> existsFollow(@PathVariable("employeeId") int employeeId,
            @PathVariable("employerId") int employerId) {
        boolean exists = followService.existsFollow(employeeId, employerId);
        return ResponseEntity.ok(Collections.singletonMap("exists", exists));
    }

  
}

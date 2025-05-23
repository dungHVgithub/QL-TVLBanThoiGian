/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.services.impl;

import com.job.pojo.JobPosting;
import com.job.repositories.JobPostingRepository;
import com.job.services.JobPostingService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author DUNG
 */
@Service
public class JobPostingServiceImpl implements JobPostingService{
    @Autowired
    private JobPostingRepository jobRepo;

    @Override
    public List<JobPosting> getJobPostings(Map<String, String> params) {
        return this.jobRepo.getJobPostings(params);
    }

    @Override
    public JobPosting getJobById(int id) {
        return this.jobRepo.getJobById(id);
    }

    @Override
    public JobPosting addOrUpdate(JobPosting j) {
        LocalDateTime now = LocalDateTime.now();
        // Chuyển đổi LocalDateTime sang Date, sử dụng múi giờ Asia/Ho_Chi_Minh (+07:00)
        Date currentDate = Date.from(now.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant());

        if (j.getId() == null) {
            // Khi add mới: đặt cả createdAt và updatedAt giống nhau
            j.setCreatedAt(currentDate);
            j.setUpdatedAt(currentDate);
        } else {
            // Khi update: chỉ cập nhật updatedAt
            j.setUpdatedAt(currentDate);
            // createdAt giữ nguyên, không cần set lại
        }
        return this.jobRepo.addOrUpdate(j);
    }

    @Override
    public void deleteJob(int id) {
        this.jobRepo.deleteJob(id);
    }

    @Override
    public long countJobs() {
        return this.jobRepo.count();
    }

    @Override
    public long countJobsByState(String state) {
        return this.jobRepo.countByState(state);
    }


}

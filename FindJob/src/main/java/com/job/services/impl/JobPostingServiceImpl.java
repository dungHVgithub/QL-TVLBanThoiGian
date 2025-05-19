/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.services.impl;

import com.job.pojo.JobPosting;
import com.job.repositories.JobPostingRepository;
import com.job.services.JobPostingService;
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

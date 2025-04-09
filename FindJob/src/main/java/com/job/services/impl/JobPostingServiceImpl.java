/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.services.impl;

import com.job.pojo.JobPosting;
import com.job.repositories.JobPostingRepository;
import com.job.services.JobPostingService;
import java.util.List;
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
    public List<JobPosting> getJobPostings() {
        return this.jobRepo.getJobPostings();
    }
    
}

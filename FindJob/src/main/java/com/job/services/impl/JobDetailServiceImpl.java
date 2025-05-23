package com.job.services.impl;

import com.job.pojo.JobDescription;
import com.job.repositories.JobDetailRepository;
import com.job.services.JobDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class JobDetailServiceImpl implements JobDetailService {

    @Autowired
    private JobDetailRepository jobDetailRepository;

    @Override
    public List<JobDescription> getJobDetail(Map<String, String> params) {
        return jobDetailRepository.getJobDetail(params);
    }

    @Override
    public void deleteJobDetail(int id) {
        this.jobDetailRepository.deleteJobDetail(id);
    }

    @Override
    public JobDescription getJobDetailById(int id) {
        JobDescription jobDescription = jobDetailRepository.getJobDetailById(id);
        if (jobDescription == null) {
            throw new RuntimeException("JobDescription not found with id: " + id);
        }
        return jobDescription;
    }

    @Override
    public JobDescription getJobDetailByJobPostingId(int jobPostingId) {
        JobDescription jobDescription = jobDetailRepository.getJobDetailByJobPostingId(jobPostingId);
        if (jobDescription == null) {
            throw new RuntimeException("JobDescription not found for JobPosting id: " + jobPostingId);
        }
        return jobDescription;
    }
}
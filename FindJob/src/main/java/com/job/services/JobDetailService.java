package com.job.services;

import com.job.pojo.JobDescription;

import java.util.List;
import java.util.Map;

public interface JobDetailService {
    List<JobDescription> getJobDetail (Map<String, String> params);
    public void deleteJobDetail (int id);
    JobDescription getJobDetailById(int id);
    JobDescription getJobDetailByJobPostingId(int jobPostingId);
    JobDescription saveJobDetail(JobDescription jobDetail);


}

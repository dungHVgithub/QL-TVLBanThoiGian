package com.job.repositories;

import com.job.pojo.JobDescription;

import java.util.Map;
import java.util.List;

public interface JobDetailRepository {
    List <JobDescription> getJobDetail (Map<String, String> params);
    public void deleteJobDetail (int id);
    JobDescription getJobDetailByJobPostingId(int jobPostingId);
    JobDescription getJobDetailById(int id);

}

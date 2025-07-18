/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.job.services;

import com.job.dto.JobPostingDTO;
import com.job.pojo.JobPosting;

import java.util.List;
import java.util.Map;

/**
 *
 * @author DUNG
 */
public interface JobPostingService {
    List<JobPosting> getJobPostings(Map<String, String> params);
    JobPosting getJobById(int id); 
    JobPosting addOrUpdate (JobPosting j);
    void deleteJob ( int id);
    long countJobs(); // Đếm tổng số JobPosting
    long countJobsByState(String state); // Đếm theo trạng thái

    List<JobPostingDTO> getJobPostingsAsDTO(Map<String, String> params);
    JobPostingDTO getJobByIdAsDTO(int id);

    List<JobPosting> getJobPostingsByEmployerId(int employerId);

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.job.repositories;

import com.job.pojo.JobPosting;
import java.util.List;
import java.util.Map;


/**
 *
 * @author DUNG
 */
public interface JobPostingRepository {
    List<JobPosting> getJobPostings(Map<String, String> params);
    JobPosting getJobById(int id); 
    JobPosting addOrUpdate(JobPosting j);
    void deleteJob(int id);
    long count(); // Đếm tổng số JobPosting
    long countByState(String state); // Đếm theo trạng thái
    long countByMonth(String month);
    long countByDate(String date); // Thêm phương thức mới
}

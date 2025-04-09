/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.job.repositories;

import com.job.pojo.JobPosting;
import java.util.List;


/**
 *
 * @author DUNG
 */
public interface JobPostingRepository {
    List<JobPosting> getJobPostings();
  
}

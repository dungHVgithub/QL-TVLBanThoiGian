/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.services.impl;

import com.job.dto.CompanyInformationDTO;
import com.job.dto.EmployerDTO;
import com.job.dto.JobPostingDTO;
import com.job.pojo.CompanyInformation;
import com.job.pojo.Employer;
import com.job.pojo.JobPosting;
import com.job.repositories.JobPostingRepository;
import com.job.services.JobPostingService;
import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    @Override
    public List<JobPostingDTO> getJobPostingsAsDTO(Map<String, String> params) {
        List<JobPosting> jobs = getJobPostings(params);
        return jobs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public JobPostingDTO getJobByIdAsDTO(int id) {
        JobPosting job = getJobById(id);
        return job != null ? convertToDTO(job) : null;
    }

    private JobPostingDTO convertToDTO(JobPosting job) {
        Employer employer = job.getEmployerId();
        EmployerDTO employerDTO = null;
        if (employer != null) {
            CompanyInformation company = employer.getCompany();
            CompanyInformationDTO companyDTO = company != null
                ? new CompanyInformationDTO(company.getId(), company.getName(), company.getAddress(), company.getTaxCode())
                : null;
            employerDTO = new EmployerDTO(employer.getId(), companyDTO);
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String timeStartStr = job.getTimeStart() != null ? timeFormat.format(job.getTimeStart()) : null;
        String timeEndStr = job.getTimeEnd() != null ? timeFormat.format(job.getTimeEnd()) : null;

        return new JobPostingDTO(
                
            job.getId(),
            job.getName(),
            job.getSalary(),
            timeStartStr,
            timeEndStr,
            job.getCategoryId() != null ? job.getCategoryId().getId() : null,
            employerDTO,
            job.getState()
        );
    }


}

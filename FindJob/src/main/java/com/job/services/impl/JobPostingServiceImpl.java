package com.job.services.impl;

import com.job.pojo.JobPosting;
import com.job.repositories.JobPostingRepository;
import com.job.services.JobPostingService;

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
public class JobPostingServiceImpl implements JobPostingService {
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
            // Khi update: lấy đối tượng hiện có từ cơ sở dữ liệu
            JobPosting existingJob = this.jobRepo.getJobById(j.getId());
            if (existingJob != null) {
                // Giữ nguyên createdAt từ đối tượng hiện có
                j.setCreatedAt(existingJob.getCreatedAt());
                // Bảo vệ timeStart và timeEnd: chỉ cập nhật nếu chúng không null trong đối tượng đầu vào
                if (j.getTimeStart() == null) {
                    j.setTimeStart(existingJob.getTimeStart());
                }
                if (j.getTimeEnd() == null) {
                    j.setTimeEnd(existingJob.getTimeEnd());
                }
            }
            // Cập nhật updatedAt
            j.setUpdatedAt(currentDate);
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

    public List<JobPosting> getJobPostingsByEmployerId(int employerId) {
        // Tạm thời sử dụng Map để truyền employerId, sau này có thể thêm logic riêng
        Map<String, String> params = Map.of("employerId", String.valueOf(employerId));
        return this.jobRepo.getJobPostings(params);
    }
}
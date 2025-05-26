package com.job.services;

import com.job.pojo.EmployeeJob;
import java.util.List;

public interface EmployeeJobService {

    // Lấy danh sách EmployeeJob theo employeeId
    List<EmployeeJob> getEmployeeJobsByEmployeeId(Integer employeeId);

    // Lấy EmployeeJob theo ID
    EmployeeJob getEmployeeJobById(Integer id);

    // Thêm hoặc cập nhật EmployeeJob
    EmployeeJob addOrUpdate(EmployeeJob employeeJob);

    // Lấy danh sách EmployeeJob theo jobId
    List<EmployeeJob> getEmployeeJobsByJobId(Integer jobId);
}
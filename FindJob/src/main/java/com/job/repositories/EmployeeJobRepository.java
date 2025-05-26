package com.job.repositories;

import com.job.pojo.EmployeeJob;
import java.util.List;

public interface EmployeeJobRepository {

    // Lấy danh sách EmployeeJob theo employeeId
    List<EmployeeJob> getEmployeeJobsByEmployeeId(Integer employeeId);

    // Lấy EmployeeJob theo ID
    EmployeeJob getEmployeeJobById(Integer id);

    // Thêm hoặc cập nhật EmployeeJob
    EmployeeJob addOrUpdate(EmployeeJob employeeJob);

    // Lấy danh sách EmployeeJob theo jobId (tùy chọn, tương tự getEmployeesByJobId)
    List<EmployeeJob> getEmployeeJobsByJobId(Integer jobId);
}
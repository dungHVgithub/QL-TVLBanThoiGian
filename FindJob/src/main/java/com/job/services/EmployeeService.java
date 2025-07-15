
package com.job.services;

import com.job.pojo.Employee;
import java.util.List;

public interface EmployeeService {
    List<Employee> getEmployees();
    Employee getEmployeeById(int id);
    Employee addOrUpdate(Employee employee);
    List<Employee> getEmployeesByJobId(int jobId);
    Integer getEmployeeIdByUserId(int userId);
}


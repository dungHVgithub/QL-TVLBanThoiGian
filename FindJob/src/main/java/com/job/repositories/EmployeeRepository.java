package com.job.repositories;

import com.job.pojo.Employee;
import java.util.List;

public interface EmployeeRepository {
    List<Employee> getEmployees();
    Employee getEmployeeById(int id);
    Employee addOrUpdate(Employee employee);
    List<Employee> getEmployeesByJobId(int jobId);
}
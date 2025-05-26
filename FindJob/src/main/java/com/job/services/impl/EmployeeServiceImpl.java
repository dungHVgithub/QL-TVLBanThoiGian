package com.job.services.impl;

import com.job.pojo.Employee;
import com.job.repositories.EmployeeRepository;
import com.job.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getEmployees() {
        return employeeRepository.getEmployees();
    }

    @Override
    public Employee getEmployeeById(int id) {
        return employeeRepository.getEmployeeById(id);
    }

    @Override
    public Employee addOrUpdate(Employee employee) {
        return employeeRepository.addOrUpdate(employee);
    }

    @Override
    public List<Employee> getEmployeesByJobId(int jobId) {
        return employeeRepository.getEmployeesByJobId(jobId);
    }
}
package com.job.services.impl;

import com.job.pojo.EmployeeJob;
import com.job.repositories.EmployeeJobRepository;
import com.job.services.EmployeeJobService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeJobServiceImpl implements EmployeeJobService {

    @Autowired
    private EmployeeJobRepository employeeJobRepository;

    @Override
    public List<EmployeeJob> getEmployeeJobsByEmployeeId(Integer employeeId) {
        return employeeJobRepository.getEmployeeJobsByEmployeeId(employeeId);
    }

    @Override
    public EmployeeJob getEmployeeJobById(Integer id) {
        return employeeJobRepository.getEmployeeJobById(id);
    }

    @Override
    public EmployeeJob addOrUpdate(EmployeeJob employeeJob) {
        return employeeJobRepository.addOrUpdate(employeeJob);
    }

    @Override
    public List<EmployeeJob> getEmployeeJobsByJobId(Integer jobId) {
        return employeeJobRepository.getEmployeeJobsByJobId(jobId);
    }
}
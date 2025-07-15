
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.job.repositories;

import com.job.pojo.Employee;
import com.job.pojo.Employer;
import com.job.pojo.Employee;
import java.util.List;

/**
 *
 * @author DUNG
 */




public interface EmployeeRepository {
    List<Employee> getEmployees();
    Employee getEmployeeById(int id);
    Employee addOrUpdate(Employee employee);
    List<Employee> getEmployeesByJobId(int jobId);
}


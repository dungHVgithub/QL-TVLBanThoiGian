
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.job.services;

/**
 *
 * @author DUNG
 */


import com.job.pojo.Employee;
import java.util.List;

public interface EmployeeService {
    Integer getEmployeeIdByUserId(int userId);
    List<Employee> getEmployees();
    Employee getEmployeeById(int id);
    Employee addOrUpdate(Employee employee);
    List<Employee> getEmployeesByJobId(int jobId);
}



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.controllers;

import com.job.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author DUNG
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiEmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employee/from_user/{userId}")
    public ResponseEntity<Integer> getEmployeeIdByUserId(@PathVariable("userId") int userId) {
        Integer employeeId = employeeService.getEmployeeIdByUserId(userId);
        if (employeeId != null)
            return ResponseEntity.ok(employeeId);
        else
            return ResponseEntity.notFound().build();
    }
}

package com.job.controllers;

import com.job.pojo.Employee;
import com.job.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ApiEmployeeController {
    @Autowired
    private EmployeeService employeeService;

    // GET: Lấy danh sách tất cả Employee
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    // GET: Lấy Employee theo ID
    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("employeeId") int id) {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            return new ResponseEntity<>(employee, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // POST: Tạo mới Employee
    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        try {
            Employee savedEmployee = employeeService.addOrUpdate(employee);
            return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // PUT: Cập nhật Employee theo ID
    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable("employeeId") int id,
            @RequestBody Employee employee) {
        try {
            Employee existingEmployee = employeeService.getEmployeeById(id);
            if (existingEmployee == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Cập nhật các trường của Employee
            existingEmployee.setId(employee.getId());
            // Cập nhật các trường khác nếu cần (tùy thuộc vào cấu trúc Employee)

            Employee updatedEmployee = employeeService.addOrUpdate(existingEmployee);
            return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET: Lấy danh sách Employee theo jobId
    @GetMapping("/employees/jobId/{jobId}")
    public ResponseEntity<List<Employee>> getEmployeesByJobId(@PathVariable("jobId") int jobId) {
        List<Employee> employees = employeeService.getEmployeesByJobId(jobId);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
}


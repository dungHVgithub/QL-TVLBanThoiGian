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

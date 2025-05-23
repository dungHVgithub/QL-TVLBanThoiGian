/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.controllers;
import com.job.pojo.Employer;
import com.job.services.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiEmployerController {
    @Autowired
    private EmployerService employerService;

    @GetMapping("/employers")
    public ResponseEntity<List<Employer>> list() {
        List<Employer> employers = employerService.getEmployers();
        return new ResponseEntity<>(employers, HttpStatus.OK);
    }

    @GetMapping("/employers/{id}")
    public ResponseEntity<Employer> retrieve(@PathVariable("id") int id) {
        Employer employer = employerService.getEmployerById(id);
        return employer != null
                ? new ResponseEntity<>(employer, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

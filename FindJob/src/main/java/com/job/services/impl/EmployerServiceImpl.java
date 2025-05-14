/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.services.impl;

import com.job.pojo.Employer;
import com.job.repositories.EmployerRepository;
import com.job.services.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployerServiceImpl implements EmployerService {
    @Autowired
    private EmployerRepository employerRepository;

    @Override
    public List<Employer> getEmployers() {
        return employerRepository.getEmployers();
    }

    @Override
    public Employer getEmployerById(int id) {
        return employerRepository.getEmployerById(id);
    }
}

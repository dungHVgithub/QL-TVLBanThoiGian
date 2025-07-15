/*
 * Click nbfs://.netbeans.org/templates/Classes/Class.java to edit this template
 */
package com.job.services.impl;

import com.job.pojo.Employer;
import com.job.pojo.User;
import com.job.repositories.EmployerRepository;
import com.job.services.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
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

    @Override
    public Employer findOrCreateEmployerByUserId(Integer userId) {
        Employer employer = employerRepository.findByUserId(userId);
        if (employer == null) {
            employer = new Employer();
            employer.setUserId(new User(userId)); // Giả định User có constructor
            employer.setCreatedAt(new Date());
        }
        return employerRepository.save(employer);
    }

    @Override
    public Employer saveEmployer(Employer employer) {
        return employerRepository.save(employer);
    }


}
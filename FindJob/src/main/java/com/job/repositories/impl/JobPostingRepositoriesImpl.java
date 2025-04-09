package com.job.repositories.impl;

import com.job.pojo.JobPosting;
import com.job.repositories.JobPostingRepository;
import jakarta.persistence.Query;

import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author DUNG
 */
@Repository
@Transactional
public class JobPostingRepositoriesImpl implements JobPostingRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<JobPosting> getJobPostings() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("From JobPosting", JobPosting.class);
        return q.getResultList();
    }

}

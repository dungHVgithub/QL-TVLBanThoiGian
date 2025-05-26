/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.repositories.impl;

import com.job.pojo.Employee;
import com.job.repositories.EmployeeRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author DUNG
 */
@Repository
@Transactional
public class EmployeeRepositoryImpl implements EmployeeRepository{
    @Autowired
    private LocalSessionFactoryBean factory;

    
    @Override
    public Integer getEmployeeIdByUserId(int userId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "SELECT e.id FROM Employee e WHERE e.userId.id = :uid";
        Query<Integer> q = s.createQuery(hql, Integer.class);
        q.setParameter("uid", userId);
        return q.uniqueResult();
    }
    
}

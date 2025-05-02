package com.job.repositories.impl;

import com.job.pojo.JobPosting;
import com.job.repositories.JobPostingRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private static final int PAGE_SIZE = 6;
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<JobPosting> getJobPostings(Map<String, String> params) {
 Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<JobPosting> q = b.createQuery(JobPosting.class);
        Root root = q.from(JobPosting.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("description"), String.format("%%%s%%", kw)));
            }

            String fromPrice = params.get("fromSalary");
            if (fromPrice != null && !fromPrice.isEmpty()) {
                predicates.add(b.greaterThanOrEqualTo(root.get("salary"), fromPrice));
            }

            String toPrice = params.get("toSalary");
            if (toPrice != null && !toPrice.isEmpty()) {
                predicates.add(b.lessThanOrEqualTo(root.get("salary"), toPrice));
            }

            String cateId = params.get("categoryId");
            if (cateId != null && !cateId.isEmpty()) {
                predicates.add(b.equal(root.get("categoryId").as(Integer.class), cateId));
            }

            q.where(predicates.toArray(Predicate[]::new));

            String orderBy = params.get("orderBy");
            if (orderBy != null && !orderBy.isEmpty()) {
                q.orderBy(b.asc(root.get(orderBy)));
            }
        }

        Query query = s.createQuery(q);

        if (params != null && params.containsKey("page")) {
            int page = Integer.parseInt(params.get("page"));
            int start = (page - 1) * PAGE_SIZE;

            query.setMaxResults(PAGE_SIZE);
            query.setFirstResult(start);
        }

        return query.getResultList();    }

    @Override
    public JobPosting getJobById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(JobPosting.class, id);
    
    }
    
    @Override
    public JobPosting addOrUpdate(JobPosting j) {
        Session s = this.factory.getObject().getCurrentSession();
        if(j.getId() == null)
        {
            s.persist(j);
        }
        else
        {
            s.merge(j);
        }
     
        return j;
    }

    @Override
    public void deleteJob(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        JobPosting j = this.getJobById(id);
        s.remove(j);
    }
   }



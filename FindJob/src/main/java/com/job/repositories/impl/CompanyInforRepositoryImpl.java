/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.repositories.impl;

import com.job.pojo.Category;
import com.job.pojo.CompanyInformation;
import com.job.pojo.JobPosting;
import com.job.repositories.CompanyInfoRepository;
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

/**
 *
 * @author DUNG
 */
@Repository
@Transactional
public class CompanyInforRepositoryImpl implements CompanyInfoRepository{
    private static final int PAGE_SIZE = 6;
    @Autowired
    private LocalSessionFactoryBean factory;
    @Override
    public List<CompanyInformation> companyInformations(Map<String, String> params) {
         Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<CompanyInformation> q = b.createQuery(CompanyInformation.class);
        Root root = q.from(CompanyInformation.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw)));
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

        return query.getResultList();   
    }

    @Override
    public CompanyInformation companyInformationById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(CompanyInformation.class, id);
    }

    @Override
    public CompanyInformation addOrUpdate(CompanyInformation ci) {
         Session s = this.factory.getObject().getCurrentSession();
        if(ci.getId() == null)
        {
            s.persist(ci);
        }
        else
        {
            s.merge(ci);
        }
     
        return ci;
    }

    @Override
    public void deleteCompanyInformation(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        CompanyInformation ci = this.companyInformationById(id);
        s.remove(ci);
    }

    

}

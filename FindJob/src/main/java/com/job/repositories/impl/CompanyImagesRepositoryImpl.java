/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.repositories.impl;

import com.job.pojo.CompanyImages;
import com.job.repositories.CompanyImagesRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class CompanyImagesRepositoryImpl implements CompanyImagesRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<CompanyImages> getAllCompanyImages() {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<CompanyImages> q = b.createQuery(CompanyImages.class);
        Root<CompanyImages> root = q.from(CompanyImages.class);
        q.select(root);

        // Fetch companyId để tránh LazyInitializationException
        root.fetch("companyId", jakarta.persistence.criteria.JoinType.LEFT);

        return s.createQuery(q).getResultList();
    }

    @Override
    public List<CompanyImages> getCompanyImagesByCompanyId(int companyId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<CompanyImages> q = b.createQuery(CompanyImages.class);
        Root<CompanyImages> root = q.from(CompanyImages.class);
        q.select(root).where(b.equal(root.get("companyId").get("id"), companyId));

        // Fetch companyId
        root.fetch("companyId", jakarta.persistence.criteria.JoinType.LEFT);

        return s.createQuery(q).getResultList();
    }

    @Override
    public void addCompanyImage(CompanyImages companyImage) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(companyImage);
    }

    @Override
    public CompanyImages getCompanyImageById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(CompanyImages.class, id);
    }

    @Override
    public void updateCompanyImage(CompanyImages companyImage) {
        Session session = this.factory.getObject().getCurrentSession();
        session.merge(companyImage); 
    }

    @Override
    public void deleteCompanyImage(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        CompanyImages companyImage = session.get(CompanyImages.class, id);
        if (companyImage != null) {
            session.remove(companyImage);
        }
    }

}

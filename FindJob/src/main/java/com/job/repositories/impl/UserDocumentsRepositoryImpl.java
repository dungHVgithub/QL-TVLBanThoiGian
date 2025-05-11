/*
 * Click nbfs://.netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://.netbeans/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.repositories.impl;

import com.job.pojo.UserDocuments;
import com.job.repositories.UserDocumentsRepository;
import jakarta.persistence.Query;
import java.util.List;
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
public class UserDocumentsRepositoryImpl implements UserDocumentsRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<UserDocuments> getDocumentses() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("UserDocuments.findAll", UserDocuments.class);
        return q.getResultList();
    }

    @Override
    public void updateStatus(UserDocuments document) {
        Session s = this.factory.getObject().getCurrentSession();
        s.merge(document); // Cập nhật entity vào DB
    }
}
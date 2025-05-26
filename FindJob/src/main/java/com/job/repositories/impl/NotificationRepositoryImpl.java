/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.repositories.impl;

import com.job.pojo.Notification;
import com.job.pojo.UserNotification;
import com.job.repositories.NotificationRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class NotificationRepositoryImpl implements NotificationRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    
    @Override
    public Notification addNotification(Notification n) {
        Session s = this.factory.getObject().getCurrentSession();
        n.setCreatedAt(new java.util.Date());
        s.persist(n);
        return n;
    }

    @Override
    public List<Notification> getNotificationsByEmployeeId(int employeeId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Notification> q = b.createQuery(Notification.class);
        Root<UserNotification> root = q.from(UserNotification.class);
        Join<UserNotification, Notification> join = root.join("notificationId");

        q.select(join).where(b.equal(root.get("employeeId").get("id"), employeeId));

        return s.createQuery(q).getResultList();
    }

  
}



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.repositories.impl;

/**
 *
 * @author User
 */
import com.job.pojo.UserNotification;
import com.job.repositories.UserNotificationRepository;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserNotificationRepositoryImpl implements UserNotificationRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public UserNotification addUserNotification(UserNotification un) {
        Session s = factory.getObject().getCurrentSession();
        s.persist(un);
        return un;
    }

    @Override
    public List<UserNotification> getNotificationsByEmployeeId(int employeeId) {
        Session s = factory.getObject().getCurrentSession();
        String hql = "FROM UserNotification u WHERE u.employeeId.id = :eid ORDER BY u.createdAt DESC";
        Query<UserNotification> q = s.createQuery(hql, UserNotification.class);
        q.setParameter("eid", employeeId);
        return q.getResultList();
    }

    @Override
    public long countUnreadNotifications(int employeeId) {
        Session s = factory.getObject().getCurrentSession();
        String hql = "SELECT COUNT(u) FROM UserNotification u WHERE u.employeeId.id = :eid AND u.isRead = false";
        Query<Long> q = s.createQuery(hql, Long.class);
        q.setParameter("eid", employeeId);
        return q.uniqueResult();
    }

    @Override
    public void markAsRead(int userNotificationId) {
//        Session s = factory.getObject().getCurrentSession();
//        UserNotification un = s.get(UserNotification.class, userNotificationId);
//        if (un != null) {
//            un.setIsRead(true);
//            un.setReadAt(new java.util.Date());
//            s.update(un);
//        }
    }
}

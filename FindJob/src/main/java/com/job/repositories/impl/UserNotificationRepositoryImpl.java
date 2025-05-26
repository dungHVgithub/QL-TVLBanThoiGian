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
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.MutationQuery;
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
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UserNotification> query = builder.createQuery(UserNotification.class);
        Root<UserNotification> root = query.from(UserNotification.class);
        query.select(root).where(
                builder.equal(root.get("employeeId").get("id"), employeeId)
        );
        return session.createQuery(query).getResultList();
    }

    @Override
    public boolean updateIsRead(int notificationId, int employeeId, boolean isRead) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();

        CriteriaUpdate<UserNotification> update = cb.createCriteriaUpdate(UserNotification.class);
        Root<UserNotification> root = update.from(UserNotification.class);

        update.set("isRead", isRead);
        update.set("readTime", isRead ? new Date() : null);

        Predicate p1 = cb.equal(root.get("notificationId").get("id"), notificationId);
        Predicate p2 = cb.equal(root.get("employeeId").get("id"), employeeId);
        update.where(cb.and(p1, p2));

        MutationQuery q = s.createMutationQuery(update);
        int updated = q.executeUpdate();
        return updated > 0;
    }

    @Override
    public long countUnreadByEmployeeId(int employeeId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<UserNotification> root = q.from(UserNotification.class);

        q.select(cb.count(root));
        Predicate p1 = cb.equal(root.get("employeeId").get("id"), employeeId);
        Predicate p2 = cb.isFalse(root.get("isRead"));
        q.where(cb.and(p1, p2));

        return s.createQuery(q).getSingleResult();
    }

}

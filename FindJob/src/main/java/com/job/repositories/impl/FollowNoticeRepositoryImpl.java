package com.job.repositories.impl;

import com.job.pojo.FollowNotice;
import com.job.repositories.FollowNoticeRepository;
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
public class FollowNoticeRepositoryImpl implements FollowNoticeRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public FollowNotice addOrUpdateFollow(FollowNotice follow) {
        Session session = this.factory.getObject().getCurrentSession();
        if (follow.getId() == null) {
            session.persist(follow);
        } else {
            session.merge(follow);
        }
        return follow;
    }

    @Override
    public List<FollowNotice> getFollowsByEmployee(int employeeId) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<FollowNotice> cq = cb.createQuery(FollowNotice.class);
        Root<FollowNotice> root = cq.from(FollowNotice.class);
        cq.where(cb.equal(root.get("employee").get("id"), employeeId));
        return session.createQuery(cq).getResultList();
    }
     @Override
    public List<FollowNotice> getFollowersByEmployer(int employerId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<FollowNotice> cq = cb.createQuery(FollowNotice.class);
        Root<FollowNotice> root = cq.from(FollowNotice.class);

        cq.select(root).where(cb.equal(root.get("employer").get("id"), employerId));

        return s.createQuery(cq).getResultList();
    }

    @Override
    public long countFollowersByEmployer(int employerId) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<FollowNotice> root = cq.from(FollowNotice.class);
        cq.select(cb.count(root)).where(cb.equal(root.get("employer").get("id"), employerId));
        return session.createQuery(cq).getSingleResult();
    }

    @Override
    public boolean deleteFollow(int employeeId, int employerId) {
        FollowNotice f = getFollowDetail(employeeId, employerId);
        if (f != null) {
            factory.getObject().getCurrentSession().remove(f);
            return true;
        }
        return false;
    }

    @Override
    public boolean existsFollow(int employeeId, int employerId) {
        Session session = factory.getObject().getCurrentSession();
        String hql = "SELECT COUNT(f) FROM FollowNotice f WHERE f.employee.id = :empId AND f.employer.id = :employerId";
        Long count = session.createQuery(hql, Long.class)
                .setParameter("empId", employeeId)
                .setParameter("employerId", employerId)
                .uniqueResult();
        return count != null && count > 0;
    }

    @Override
    public FollowNotice getFollowDetail(int employeeId, int employerId) {
        Session session = factory.getObject().getCurrentSession();
        String hql = "FROM FollowNotice f WHERE f.employee.id = :empId AND f.employer.id = :employerId";
        return session.createQuery(hql, FollowNotice.class)
                .setParameter("empId", employeeId)
                .setParameter("employerId", employerId)
                .uniqueResult();
    }

   
}

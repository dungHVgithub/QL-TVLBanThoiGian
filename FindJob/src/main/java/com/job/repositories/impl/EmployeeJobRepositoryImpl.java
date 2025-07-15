package com.job.repositories.impl;

import com.job.pojo.EmployeeJob;
import com.job.repositories.EmployeeJobRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional
public class EmployeeJobRepositoryImpl implements EmployeeJobRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<EmployeeJob> getEmployeeJobsByEmployeeId(Integer employeeId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<EmployeeJob> q = b.createQuery(EmployeeJob.class);
        Root<EmployeeJob> root = q.from(EmployeeJob.class);
        q.select(root).where(b.equal(root.get("employeeId").get("id"), employeeId));
        // Fetch các mối quan hệ để tránh lazy loading
        root.fetch("employeeId", jakarta.persistence.criteria.JoinType.LEFT);
        root.fetch("jobId", jakarta.persistence.criteria.JoinType.LEFT);
        return s.createQuery(q).getResultList();
    }

    @Override
    public EmployeeJob getEmployeeJobById(Integer id) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<EmployeeJob> q = b.createQuery(EmployeeJob.class);
        Root<EmployeeJob> root = q.from(EmployeeJob.class);
        q.select(root).where(b.equal(root.get("id"), id));
        // Fetch các mối quan hệ để tránh lazy loading
        root.fetch("employeeId", jakarta.persistence.criteria.JoinType.LEFT);
        root.fetch("jobId", jakarta.persistence.criteria.JoinType.LEFT);
        return s.createQuery(q).getSingleResultOrNull();
    }

    @Override
    public EmployeeJob addOrUpdate(EmployeeJob employeeJob) {
        Session s = this.factory.getObject().getCurrentSession();
        if (employeeJob.getId() == null) {
            s.persist(employeeJob);
        } else {
            s.merge(employeeJob); // Cập nhật toàn bộ đối tượng, bao gồm jobState
        }
        return employeeJob;
    }

    @Override
    public List<EmployeeJob> getEmployeeJobsByJobId(Integer jobId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<EmployeeJob> q = b.createQuery(EmployeeJob.class);
        Root<EmployeeJob> root = q.from(EmployeeJob.class);
        q.select(root).where(b.equal(root.get("jobId").get("id"), jobId));
        // Fetch các mối quan hệ để tránh lazy loading
        root.fetch("employeeId", jakarta.persistence.criteria.JoinType.LEFT);
        root.fetch("jobId", jakarta.persistence.criteria.JoinType.LEFT);
        return s.createQuery(q).getResultList();
    }
}
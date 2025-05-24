package com.job.repositories.impl;
import com.job.pojo.JobDescription;
import com.job.repositories.JobDetailRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class JobDetailRepositoryImpl implements JobDetailRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<JobDescription> getJobDetail(Map<String, String> params) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<JobDescription> query = builder.createQuery(JobDescription.class);
        Root<JobDescription> root = query.from(JobDescription.class);
        query.select(root);

        // Xây dựng điều kiện lọc
        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            // Lọc theo description (Job_Description)
            String description = params.get("Job_Description");
            if (description != null && !description.isEmpty()) {
                predicates.add(builder.like(root.get("description"), String.format("%%%s%%", description)));
            }

            // Lọc theo type (Skill_Requirement)
            String type = params.get("Skill_Requirement");
            if (type != null && !type.isEmpty()) {
                predicates.add(builder.like(root.get("type"), String.format("%%%s%%", type)));
            }

            query.where(predicates.toArray(Predicate[]::new));
        }

        Query finalQuery = session.createQuery(query);

        return finalQuery.getResultList();
    }

    @Override
    public void deleteJobDetail(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        JobDescription j = this.getJobDetailById(id);
        s.remove(j);
    }

    @Override
    public JobDescription getJobDetailByJobPostingId(int jobPostingId) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<JobDescription> query = builder.createQuery(JobDescription.class);
        Root<JobDescription> root = query.from(JobDescription.class);
        query.select(root);
        // Điều kiện lọc: jobPosting.id = jobPostingId
        query.where(builder.equal(root.get("jobPosting").get("id"), jobPostingId));
        Query finalQuery = session.createQuery(query);
        List<JobDescription> results = finalQuery.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public JobDescription getJobDetailById(int id) {
        Session session = factory.getObject().getCurrentSession();
        return session.get(JobDescription.class, id);    }

    @Override
    public JobDescription save(JobDescription jobDetail) {
        Session session = factory.getObject().getCurrentSession();
        if (jobDetail.getId() == null) {
            session.persist(jobDetail); // Thêm mới
        } else {
            session.merge(jobDetail); // Cập nhật
        }
        return jobDetail;
    }
}
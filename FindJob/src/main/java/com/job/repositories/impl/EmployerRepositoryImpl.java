package com.job.repositories.impl;
import com.job.pojo.Employer;
import com.job.repositories.EmployerRepository;
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
public class EmployerRepositoryImpl implements EmployerRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Employer> getEmployers() {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Employer> q = b.createQuery(Employer.class);
        Root<Employer> root = q.from(Employer.class);
        q.select(root);
        root.fetch("company", jakarta.persistence.criteria.JoinType.LEFT);
        return s.createQuery(q).getResultList();
    }

    @Override
    public Employer getEmployerById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Employer> q = b.createQuery(Employer.class);
        Root<Employer> root = q.from(Employer.class);
        q.select(root).where(b.equal(root.get("id"), id));
        root.fetch("company", jakarta.persistence.criteria.JoinType.LEFT);
        return s.createQuery(q).getSingleResultOrNull();
    }

}
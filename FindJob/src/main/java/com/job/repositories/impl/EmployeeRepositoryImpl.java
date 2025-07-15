
package com.job.repositories.impl;

import com.job.pojo.Employee;
import com.job.repositories.EmployeeRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


/**
 *
 * @author DUNG
 */


@Repository
@Transactional
public class EmployeeRepositoryImpl implements EmployeeRepository {
    @Autowired
    private LocalSessionFactoryBean factory;
    @Override
    public Integer getEmployeeIdByUserId(int userId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "SELECT e.id FROM Employee e WHERE e.userId.id = :uid";
        Query<Integer> q = s.createQuery(hql, Integer.class);
        q.setParameter("uid", userId);
        return q.uniqueResult();
    }

    @Override
    public List<Employee> getEmployees() {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Employee> q = b.createQuery(Employee.class);
        Root<Employee> root = q.from(Employee.class);
        q.select(root);
        root.fetch("userId", jakarta.persistence.criteria.JoinType.LEFT); // Fetch userId (nếu có)
        return s.createQuery(q).getResultList();
    }

    @Override
    public Employee getEmployeeById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Employee> q = b.createQuery(Employee.class);
        Root<Employee> root = q.from(Employee.class);
        q.select(root).where(b.equal(root.get("id"), id));
        root.fetch("userId", jakarta.persistence.criteria.JoinType.LEFT); // Fetch userId (nếu có)
        return s.createQuery(q).getSingleResultOrNull();
    }

    @Override
    public Employee addOrUpdate(Employee employee) {
        Session s = this.factory.getObject().getCurrentSession();
        if (employee.getId() == null) {
            s.persist(employee);
        } else {
            s.merge(employee);
        }
        return employee;
    }

    @Override
    public List<Employee> getEmployeesByJobId(int jobId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Employee> q = b.createQuery(Employee.class);
        Root<Employee> root = q.from(Employee.class);
        q.select(root).where(b.equal(root.get("jobId").get("id"), jobId)); // So sánh với jobId.id
        root.fetch("userId", jakarta.persistence.criteria.JoinType.LEFT); // Fetch userId (nếu có)
        return s.createQuery(q).getResultList();
    }
}


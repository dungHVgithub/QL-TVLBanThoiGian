/*
 * Click nbfs://.netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://.netbeans/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.repositories.impl;


import com.job.pojo.UserDocuments;
import com.job.repositories.UserDocRepository;
import org.hibernate.query.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.io.File;
import java.util.List;
import java.util.Map;
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
public class UserDocRepositoryImpl implements UserDocRepository {

    private static final int PAGE_SIZE = 6;
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<UserDocuments> getUserDocs(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<UserDocuments> q = b.createQuery(UserDocuments.class);
        Root root = q.from(UserDocuments.class);
        q.select(root);

        Query query = s.createQuery(q);

        if (params != null && params.containsKey("page")) {
            int page = Integer.parseInt(params.get("page"));
            int start = (page - 1) * PAGE_SIZE;

            query.setMaxResults(PAGE_SIZE);
            query.setFirstResult(start);
        }

        return query.getResultList();
    }

    @Override
    public UserDocuments getUserDocsById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(UserDocuments.class, id);
    }

    @Override
    public UserDocuments addOrUpdate(UserDocuments j) {
        Session s = this.factory.getObject().getCurrentSession();
        if (j.getId() == null) {
            s.persist(j);
        } else {
            s.merge(j);
        }

        return j;
    }

    @Override
    public void deleteUserDocs(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        UserDocuments j = this.getUserDocsById(id);
        s.remove(j);
    }

    public UserDocRepositoryImpl() {
        String path = new File("src/main/resources/cccd-gcv.json").getAbsolutePath();
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", path);
    }

    public List<UserDocuments> getDocsByUserId(int userId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "FROM UserDocuments u WHERE u.employeeId.userId.id = :uid";
        Query<UserDocuments> q = s.createQuery(hql, UserDocuments.class);
        q.setParameter("uid", userId);
        return q.getResultList();
    }

    // Thêm phương thức để lấy danh sách tài liệu theo employeeId
    public List<UserDocuments> getDocsByEmployeeId(int employeeId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query<UserDocuments> q = s.createNamedQuery("UserDocuments.findByEmployeeId", UserDocuments.class);
        q.setParameter("employeeId", employeeId);
        return q.getResultList();
    }

    // Thêm hoặc cập nhật tài liệu cho employeeId cụ thể
    public UserDocuments addOrUpdateForEmployee(UserDocuments doc, int employeeId) {
        Session s = this.factory.getObject().getCurrentSession();
        Employee emp = s.get(Employee.class, employeeId); // Lấy employee từ DB
        if (emp == null) {
            throw new IllegalArgumentException("Employee not found with ID: " + employeeId);
        }
        doc.setEmployeeId(emp); // Gán employeeId cho tài liệu
        if (doc.getId() == null) {
            s.persist(doc);
        } else {
            s.merge(doc);
        }
        return doc;
    }
}
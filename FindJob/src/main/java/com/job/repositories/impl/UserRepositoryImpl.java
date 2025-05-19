package com.job.repositories.impl;

import com.job.pojo.User;
import com.job.repositories.UserRepository;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    private static final int PAGE_SIZE = 6;
    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User getUserByUserName(String username) {
        Session s = this.factory.getObject().getCurrentSession();
        try {
            Query q = s.createNamedQuery("User.findByUsername", User.class);
            q.setParameter("username", username);
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public User addUpdateUser(User u) {
        Session s = this.factory.getObject().getCurrentSession();
        if (u.getId() == null) {
            s.persist(u);
        } else {
            s.merge(u);
        }
        return u;
    }

    @Override
    public boolean authenticate(String username, String password) {
        User u = this.getUserByUserName(username);
        return this.passwordEncoder.matches(password, u.getPassword());
    }

    @Override
    public List<User> getUser(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<User> q = b.createQuery(User.class);
        Root root = q.from(User.class);
        q.select(root);
        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw)));
            }
            String sdt = params.get("sdt");
            if (sdt != null && !sdt.isEmpty()) {
                predicates.add(b.like(root.get("sdt"), String.format("%%%s%%", sdt)));
            }
            String role = params.get("role");
            if (role != null && !role.isEmpty()) {
                predicates.add(b.like(root.get("role"), String.format("%%%s%%", role)));
            }
            q.where(predicates.toArray(Predicate[]::new));
            String orderBy = params.get("orderBy");
            if (orderBy != null && !orderBy.isEmpty()) {
                q.orderBy(b.asc(root.get(orderBy)));
            }
        }
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
    public User getUserById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(User.class, id);
    }

    @Override
    public void deleteUser(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        User u = this.getUserById(id);
        s.remove(u);
    }

    @Override
    public User getUserByEmail(String email) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("User.findByEmail", User.class);
        q.setParameter("email", email);
        try {
            return (User) q.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Lỗi khi truy vấn user theo email");
        }
    }

    @Override
    public long countByRole(String role) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<User> root = q.from(User.class);
        q.select(b.count(root));
        q.where(b.equal(root.get("role"), role));
        return s.createQuery(q).getSingleResult();
    }

    @Override
    public long countByRoleAndMonth(String role, String month) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<User> root = q.from(User.class);
        q.select(b.count(root));
        q.where(
                b.and(
                        b.equal(root.get("role"), role),
                        b.like(b.function("DATE_FORMAT", String.class, root.get("createdAt"), b.literal("%Y-%m")), month)
                )
        );
        return s.createQuery(q).getSingleResult();
    }

    @Override
    public long countByRoleAndDate(String role, String date) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<User> root = q.from(User.class);
        q.select(b.count(root));
        q.where(
                b.and(
                        b.equal(root.get("role"), role),
                        b.like(b.function("DATE_FORMAT", String.class, root.get("createdAt"), b.literal("%Y-%m-%d")), date)
                )
        );
        return s.createQuery(q).getSingleResult();
    }
}
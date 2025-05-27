/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author AN515-57
 */
@Entity
@Table(name = "employee")
@NamedQueries({
    @NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e"),
    @NamedQuery(name = "Employee.findById", query = "SELECT e FROM Employee e WHERE e.id = :id")})
public class Employee implements Serializable {

    @Size(max = 50)
    @Column(name = "level")
    private String level;
    @OneToMany(mappedBy = "employeeId")
    @JsonIgnore
    private Set<UserNotification> userNotificationSet;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @OneToMany(mappedBy = "employeeId")
    @JsonIgnore
    private Collection<JobPosting> jobPostingCollection;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OneToOne
    private User userId;
    @OneToMany(mappedBy = "employeeId")
    @JsonIgnore
    private Collection<EmployeeJob> employeeJobCollection;

    public Employee() {
    }

    public Employee(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Collection<JobPosting> getJobPostingCollection() {
        return jobPostingCollection;
    }

    public void setJobPostingCollection(Collection<JobPosting> jobPostingCollection) {
        this.jobPostingCollection = jobPostingCollection;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Collection<EmployeeJob> getEmployeeJobCollection() {
        return employeeJobCollection;
    }

    public void setEmployeeJobCollection(Collection<EmployeeJob> employeeJobCollection) {
        this.employeeJobCollection = employeeJobCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.job.pojo.Employee[ id=" + id + " ]";
    }
    public Set<UserNotification> getUserNotificationSet() {
        return userNotificationSet;
    }

    public void setUserNotificationSet(Set<UserNotification> userNotificationSet) {
        this.userNotificationSet = userNotificationSet;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    
}

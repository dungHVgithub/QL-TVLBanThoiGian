/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
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
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author AN515-57
 */
@Entity
@Table(name = "employee")
@NamedQueries({
    @NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e"),
    @NamedQuery(name = "Employee.findById", query = "SELECT e FROM Employee e WHERE e.id = :id"),
    @NamedQuery(name = "Employee.findByLevel", query = "SELECT e FROM Employee e WHERE e.level = :level")})
public class Employee implements Serializable {

    @Size(max = 50)
    @Column(name = "level")
    private String level;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Set<HosoUngtuyen> hosoUngtuyenSet;
    @OneToMany(mappedBy = "employeeId")
    private Set<UserDocuments> userDocumentsSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Set<FollowNotice> followNoticeSet;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OneToOne
    private User userId;

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


    public Set<HosoUngtuyen> getHosoUngtuyenSet() {
        return hosoUngtuyenSet;
    }

    public void setHosoUngtuyenSet(Set<HosoUngtuyen> hosoUngtuyenSet) {
        this.hosoUngtuyenSet = hosoUngtuyenSet;
    }

    public Set<UserDocuments> getUserDocumentsSet() {
        return userDocumentsSet;
    }

    public void setUserDocumentsSet(Set<UserDocuments> userDocumentsSet) {
        this.userDocumentsSet = userDocumentsSet;
    }

    public Set<FollowNotice> getFollowNoticeSet() {
        return followNoticeSet;
    }

    public void setFollowNoticeSet(Set<FollowNotice> followNoticeSet) {
        this.followNoticeSet = followNoticeSet;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
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


    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
    
}

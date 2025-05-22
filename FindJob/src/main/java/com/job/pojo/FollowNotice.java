/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * @author AN515-57
 */
@Entity
@Table(name = "follow_notice")
@NamedQueries({
    @NamedQuery(name = "FollowNotice.findAll", query = "SELECT f FROM FollowNotice f"),
    @NamedQuery(name = "FollowNotice.findByEmployeeId", query = "SELECT f FROM FollowNotice f WHERE f.followNoticePK.employeeId = :employeeId"),
    @NamedQuery(name = "FollowNotice.findByEmployerId", query = "SELECT f FROM FollowNotice f WHERE f.followNoticePK.employerId = :employerId"),
    @NamedQuery(name = "FollowNotice.findByIsFollow", query = "SELECT f FROM FollowNotice f WHERE f.isFollow = :isFollow")})
public class FollowNotice implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FollowNoticePK followNoticePK;
    @Lob
    @Size(max = 65535)
    @Column(name = "notice")
    private String notice;
    @Column(name = "isFollow")
    private Boolean isFollow;
    @JoinColumn(name = "employee_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Employee employee;
    @JoinColumn(name = "employer_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Employer employer;

    public FollowNotice() {
    }

    public FollowNotice(FollowNoticePK followNoticePK) {
        this.followNoticePK = followNoticePK;
    }

    public FollowNotice(int employeeId, int employerId) {
        this.followNoticePK = new FollowNoticePK(employeeId, employerId);
    }

    public FollowNoticePK getFollowNoticePK() {
        return followNoticePK;
    }

    public void setFollowNoticePK(FollowNoticePK followNoticePK) {
        this.followNoticePK = followNoticePK;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public Boolean getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(Boolean isFollow) {
        this.isFollow = isFollow;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (followNoticePK != null ? followNoticePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FollowNotice)) {
            return false;
        }
        FollowNotice other = (FollowNotice) object;
        if ((this.followNoticePK == null && other.followNoticePK != null) || (this.followNoticePK != null && !this.followNoticePK.equals(other.followNoticePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.job.pojo.FollowNotice[ followNoticePK=" + followNoticePK + " ]";
    }
    
}

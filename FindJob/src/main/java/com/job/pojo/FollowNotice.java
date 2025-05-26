package com.job.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "follow_notice")
@NamedQueries({
    @NamedQuery(name = "FollowNotice.findAll", query = "SELECT f FROM FollowNotice f"),
    @NamedQuery(name = "FollowNotice.findById", query = "SELECT f FROM FollowNotice f WHERE f.id = :id"),
    @NamedQuery(name = "FollowNotice.findByFollowTime", query = "SELECT f FROM FollowNotice f WHERE f.followTime = :followTime")
})
public class FollowNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "follow_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date followTime;

    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Employee employee;

    @JoinColumn(name = "employer_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Employer employer;

    public FollowNotice() {
    }

    public FollowNotice(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFollowTime() {
        return followTime;
    }

    public void setFollowTime(Date followTime) {
        this.followTime = followTime;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FollowNotice)) {
            return false;
        }
        FollowNotice other = (FollowNotice) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "com.job.pojo.FollowNotice[ id=" + id + " ]";
    }
}

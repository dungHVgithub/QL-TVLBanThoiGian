/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author DUNG
 */
@Entity
@Table(name = "employer")
@NamedQueries({
    @NamedQuery(name = "Employer.findAll", query = "SELECT e FROM Employer e"),
    @NamedQuery(name = "Employer.findById", query = "SELECT e FROM Employer e WHERE e.id = :id"),
    @NamedQuery(name = "Employer.findByCompany", query = "SELECT e FROM Employer e WHERE e.company = :company")})
public class Employer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "company")
    private Integer company;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employer")
    @JsonIgnore
    private Set<FollowNotice> followNoticeSet;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OneToOne
    @JsonIgnore
    private User userId;
    @OneToMany(mappedBy = "employerId")
    @JsonIgnore
    private Set<JobPosting> jobPostingSet;

    public Employer() {
    }

    public Employer(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompany() {
        return company;
    }

    public void setCompany(Integer company) {
        this.company = company;
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

    public Set<JobPosting> getJobPostingSet() {
        return jobPostingSet;
    }

    public void setJobPostingSet(Set<JobPosting> jobPostingSet) {
        this.jobPostingSet = jobPostingSet;
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
        if (!(object instanceof Employer)) {
            return false;
        }
        Employer other = (Employer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.job.pojo.Employer[ id=" + id + " ]";
    }

}

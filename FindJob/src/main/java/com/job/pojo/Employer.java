package com.job.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "employer")
@NamedQueries({
        @NamedQuery(name = "Employer.findAll", query = "SELECT e FROM Employer e"),
        @NamedQuery(name = "Employer.findById", query = "SELECT e FROM Employer e WHERE e.id = :id"),
        @NamedQuery(name = "Employer.findByCompany", query = "SELECT e FROM Employer e WHERE e.company.id = :company")})
public class Employer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "company", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CompanyInformation company;

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

    // Thêm trường createdAt
    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

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

    public CompanyInformation getCompany() {
        return company;
    }

    public void setCompany(CompanyInformation company) {
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
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
package com.job.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "employer")
@NamedQueries({
        @NamedQuery(name = "Employer.findAll", query = "SELECT e FROM Employer e"),
        @NamedQuery(name = "Employer.findById", query = "SELECT e FROM Employer e WHERE e.id = :id"),
        @NamedQuery(name = "Employer.findByCreatedAt", query = "SELECT e FROM Employer e WHERE e.createdAt = :createdAt")
})
public class Employer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employer")
    @JsonIgnore
    private Set<FollowNotice> followNoticeSet;

    @OneToMany(mappedBy = "employerId")
    @JsonIgnore
    private Set<Notification> notificationSet;

    @JoinColumn(name = "company", referencedColumnName = "id")
    @ManyToOne
    @JsonManagedReference
    private CompanyInformation company;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OneToOne
    private User userId;

    @OneToMany(mappedBy = "employerId")
    @JsonIgnore
    private Collection<JobPosting> jobPostingCollection;

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Set<FollowNotice> getFollowNoticeSet() {
        return followNoticeSet;
    }

    public void setFollowNoticeSet(Set<FollowNotice> followNoticeSet) {
        this.followNoticeSet = followNoticeSet;
    }

    public Set<Notification> getNotificationSet() {
        return notificationSet;
    }

    public void setNotificationSet(Set<Notification> notificationSet) {
        this.notificationSet = notificationSet;
    }

    public CompanyInformation getCompany() {
        return company;
    }

    public void setCompany(CompanyInformation company) {
        this.company = company;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Collection<JobPosting> getJobPostingCollection() {
        return jobPostingCollection;
    }

    public void setJobPostingCollection(Collection<JobPosting> jobPostingCollection) {
        this.jobPostingCollection = jobPostingCollection;
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
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "com.job.pojo.Employer[ id=" + id + " ]";
    }
}

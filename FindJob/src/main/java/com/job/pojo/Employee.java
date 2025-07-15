package com.job.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "employee")
@NamedQueries({
        @NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e"),
        @NamedQuery(name = "Employee.findById", query = "SELECT e FROM Employee e WHERE e.id = :id")
})
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @OneToMany(mappedBy = "employeeId")
    @JsonIgnore
    private Set<UserDocuments> userDocumentsSet;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    @JsonIgnore
    private Set<FollowNotice> followNoticeSet;

    @OneToMany(mappedBy = "employeeId")
    @JsonIgnore
    private Set<UserNotification> userNotificationSet;

    @OneToMany(mappedBy = "employeeId")
    @JsonIgnore
    private Set<EmployeeJob> employeeJobSet;

    @OneToMany(mappedBy = "employeeId")
    @JsonIgnore
    private Set<JobPosting> jobPostingSet;

    @OneToMany(mappedBy = "employeeId")
    @JsonIgnore
    private Set<HosoUngtuyen> hosoUngtuyenSet;

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

    public Set<UserNotification> getUserNotificationSet() {
        return userNotificationSet;
    }

    public void setUserNotificationSet(Set<UserNotification> userNotificationSet) {
        this.userNotificationSet = userNotificationSet;
    }

    public Set<EmployeeJob> getEmployeeJobSet() {
        return employeeJobSet;
    }

    public void setEmployeeJobSet(Set<EmployeeJob> employeeJobSet) {
        this.employeeJobSet = employeeJobSet;
    }

    public Set<JobPosting> getJobPostingSet() {
        return jobPostingSet;
    }

    public void setJobPostingSet(Set<JobPosting> jobPostingSet) {
        this.jobPostingSet = jobPostingSet;
    }

    public Set<HosoUngtuyen> getHosoUngtuyenSet() {
        return hosoUngtuyenSet;
    }

    public void setHosoUngtuyenSet(Set<HosoUngtuyen> hosoUngtuyenSet) {
        this.hosoUngtuyenSet = hosoUngtuyenSet;
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
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "com.job.pojo.Employee[ id=" + id + " ]";
    }
}

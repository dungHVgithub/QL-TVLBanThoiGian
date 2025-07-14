package com.job.pojo;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "follow_notice")
@NamedQueries({
    @NamedQuery(name = "FollowNotice.findAll", query = "SELECT f FROM FollowNotice f"),
    @NamedQuery(name = "FollowNotice.findByEmployeeId", query = "SELECT f FROM FollowNotice f WHERE f.employeeId = :employeeId"),
    @NamedQuery(name = "FollowNotice.findByEmployerId", query = "SELECT f FROM FollowNotice f WHERE f.employerId = :employerId")
})
public class FollowNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "employee_id", nullable = false)
    private Integer employeeId;

    @Column(name = "employer_id", nullable = false)
    private Integer employerId;

    @Column(name = "follow_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date followTime = new Date();

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "employer_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Employer employer;

    public FollowNotice() {}

    public FollowNotice(Integer employeeId, Integer employerId) {
        this.employeeId = employeeId;
        this.employerId = employerId;
        this.followTime = new Date();
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Integer employerId) {
        this.employerId = employerId;
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
    public String toString() {
        return "FollowNotice{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", employerId=" + employerId +
                ", followTime=" + followTime +
                '}';
    }
}

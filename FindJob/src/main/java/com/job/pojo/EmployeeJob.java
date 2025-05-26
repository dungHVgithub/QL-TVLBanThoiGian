/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import java.io.Serializable;

/**
 *
 * @author AN515-57
 */
@Entity
@Table(name = "employee_job")
@NamedQueries({
    @NamedQuery(name = "EmployeeJob.findAll", query = "SELECT e FROM EmployeeJob e"),
    @NamedQuery(name = "EmployeeJob.findById", query = "SELECT e FROM EmployeeJob e WHERE e.id = :id"),
    @NamedQuery(name = "EmployeeJob.findByJobState", query = "SELECT e FROM EmployeeJob e WHERE e.jobState = :jobState"),
    @NamedQuery(name = "EmployeeJob.findByFavoriteJob", query = "SELECT e FROM EmployeeJob e WHERE e.favoriteJob = :favoriteJob")})
public class EmployeeJob implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "jobState")
    private Short jobState;
    @Column(name = "favoriteJob")
    private Short favoriteJob;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne
    private Employee employeeId;
    @JoinColumn(name = "job_id", referencedColumnName = "id")
    @ManyToOne
    private JobPosting jobId;

    public EmployeeJob() {
    }

    public EmployeeJob(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Short getJobState() {
        return jobState;
    }

    public void setJobState(Short jobState) {
        this.jobState = jobState;
    }

    public Short getFavoriteJob() {
        return favoriteJob;
    }

    public void setFavoriteJob(Short favoriteJob) {
        this.favoriteJob = favoriteJob;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }

    public JobPosting getJobId() {
        return jobId;
    }

    public void setJobId(JobPosting jobId) {
        this.jobId = jobId;
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
        if (!(object instanceof EmployeeJob)) {
            return false;
        }
        EmployeeJob other = (EmployeeJob) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.job.pojo.EmployeeJob[ id=" + id + " ]";
    }
    
}

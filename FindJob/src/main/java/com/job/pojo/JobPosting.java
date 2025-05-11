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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author DUNG
 */
@Entity
@Table(name = "job_posting")
@NamedQueries({
    @NamedQuery(name = "JobPosting.findAll", query = "SELECT j FROM JobPosting j"),
    @NamedQuery(name = "JobPosting.findById", query = "SELECT j FROM JobPosting j WHERE j.id = :id"),
    @NamedQuery(name = "JobPosting.findBySalary", query = "SELECT j FROM JobPosting j WHERE j.salary = :salary"),
    @NamedQuery(name = "JobPosting.findByTimeStart", query = "SELECT j FROM JobPosting j WHERE j.timeStart = :timeStart"),
    @NamedQuery(name = "JobPosting.findByTimeEnd", query = "SELECT j FROM JobPosting j WHERE j.timeEnd = :timeEnd"),
    @NamedQuery(name = "JobPosting.findByState", query = "SELECT j FROM JobPosting j WHERE j.state = :state"),
    @NamedQuery(name = "JobPosting.findBySubmitEnd", query = "SELECT j FROM JobPosting j WHERE j.submitEnd = :submitEnd")})
public class JobPosting implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Lob
    @Size(max = 65535)
    @Column(name = "description")
    private String description;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "salary")
    private Double salary;
    @Column(name = "time_start")
    @Temporal(TemporalType.TIME)
    private Date timeStart;
    @Column(name = "time_end")
    @Temporal(TemporalType.TIME)
    private Date timeEnd;
    @Size(max = 8)
    @Column(name = "state")
    private String state;
    @Column(name = "submit_end")
    @Temporal(TemporalType.DATE)
    private Date submitEnd;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jobPosting")
    @JsonIgnore
    private Set<HosoUngtuyen> hosoUngtuyenSet;
    @OneToMany(mappedBy = "jobPostingId")
    @JsonIgnore
    private Set<JobDescription> jobDescriptionSet;
    @JoinColumn(name = "approved_by_admin_id", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private Admin approvedByAdminId;
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private Category categoryId;
    @JoinColumn(name = "employer_id", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private Employer employerId;

    public JobPosting() {
    }

    public JobPosting(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getSubmitEnd() {
        return submitEnd;
    }

    public void setSubmitEnd(Date submitEnd) {
        this.submitEnd = submitEnd;
    }

    public Set<HosoUngtuyen> getHosoUngtuyenSet() {
        return hosoUngtuyenSet;
    }

    public void setHosoUngtuyenSet(Set<HosoUngtuyen> hosoUngtuyenSet) {
        this.hosoUngtuyenSet = hosoUngtuyenSet;
    }

    public Set<JobDescription> getJobDescriptionSet() {
        return jobDescriptionSet;
    }

    public void setJobDescriptionSet(Set<JobDescription> jobDescriptionSet) {
        this.jobDescriptionSet = jobDescriptionSet;
    }

    public Admin getApprovedByAdminId() {
        return approvedByAdminId;
    }

    public void setApprovedByAdminId(Admin approvedByAdminId) {
        this.approvedByAdminId = approvedByAdminId;
    }

    public Category getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Category categoryId) {
        this.categoryId = categoryId;
    }

    public Employer getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Employer employerId) {
        this.employerId = employerId;
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
        if (!(object instanceof JobPosting)) {
            return false;
        }
        JobPosting other = (JobPosting) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.job.pojo.JobPosting[ id=" + id + " ]";
    }
    
}

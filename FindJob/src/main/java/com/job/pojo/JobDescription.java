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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author DUNG
 */
@Entity
@Table(name = "job_description")
@NamedQueries({
    @NamedQuery(name = "JobDescription.findAll", query = "SELECT j FROM JobDescription j"),
    @NamedQuery(name = "JobDescription.findById", query = "SELECT j FROM JobDescription j WHERE j.id = :id"),
    @NamedQuery(name = "JobDescription.findByLevel", query = "SELECT j FROM JobDescription j WHERE j.level = :level"),
    @NamedQuery(name = "JobDescription.findByExperience", query = "SELECT j FROM JobDescription j WHERE j.experience = :experience"),
    @NamedQuery(name = "JobDescription.findBySubmitEnd", query = "SELECT j FROM JobDescription j WHERE j.submitEnd = :submitEnd")})
public class JobDescription implements Serializable {

    @Lob
    @Size(max = 65535)
    @Column(name = "description")
    private String description;
    @Size(max = 6)
    @Column(name = "level")
    private String level;
    @Size(max = 50)
    @Column(name = "experience")
    private String experience;
    @Lob
    @Size(max = 65535)
    @Column(name = "benefit")
    private String benefit;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "submit_end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date submitEnd;
    @JoinColumn(name = "job_posting", referencedColumnName = "id")
    @ManyToOne
    private JobPosting jobPosting;

    public JobDescription() {
    }

    public JobDescription(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Date getSubmitEnd() {
        return submitEnd;
    }

    public void setSubmitEnd(Date submitEnd) {
        this.submitEnd = submitEnd;
    }


    public JobPosting getJobPosting() {
        return jobPosting;
    }

    public void setJobPosting(JobPosting jobPosting) {
        this.jobPosting = jobPosting;
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
        if (!(object instanceof JobDescription)) {
            return false;
        }
        JobDescription other = (JobDescription) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.job.pojo.JobDescription[ id=" + id + " ]";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getBenefit() {
        return benefit;
    }

    public void setBenefit(String benefit) {
        this.benefit = benefit;
    }
    
}

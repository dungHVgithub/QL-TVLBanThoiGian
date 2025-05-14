/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * @author DUNG
 */
@Entity
@Table(name = "job_description")
@NamedQueries({
    @NamedQuery(name = "JobDescription.findAll", query = "SELECT j FROM JobDescription j"),
    @NamedQuery(name = "JobDescription.findById", query = "SELECT j FROM JobDescription j WHERE j.id = :id"),
    @NamedQuery(name = "JobDescription.findByType", query = "SELECT j FROM JobDescription j WHERE j.type = :type")})
public class JobDescription implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Lob
    @Size(max = 65535)
    @Column(name = "description")
    private String description;
    @Size(max = 17)
    @Column(name = "type")
    private String type;
    @JoinColumn(name = "job_posting_id", referencedColumnName = "id")
    @ManyToOne
     @JsonIgnore
    private JobPosting jobPostingId;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JobPosting getJobPostingId() {
        return jobPostingId;
    }

    public void setJobPostingId(JobPosting jobPostingId) {
        this.jobPostingId = jobPostingId;
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
    
}

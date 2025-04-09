/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * @author DUNG
 */
@Embeddable
public class HosoUngtuyenPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "job_id")
    private int jobId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "employee_id")
    private int employeeId;

    public HosoUngtuyenPK() {
    }

    public HosoUngtuyenPK(int jobId, int employeeId) {
        this.jobId = jobId;
        this.employeeId = employeeId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) jobId;
        hash += (int) employeeId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HosoUngtuyenPK)) {
            return false;
        }
        HosoUngtuyenPK other = (HosoUngtuyenPK) object;
        if (this.jobId != other.jobId) {
            return false;
        }
        if (this.employeeId != other.employeeId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.job.pojo.HosoUngtuyenPK[ jobId=" + jobId + ", employeeId=" + employeeId + " ]";
    }
    
}

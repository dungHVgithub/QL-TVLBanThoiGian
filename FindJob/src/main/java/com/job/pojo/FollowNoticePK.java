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
 * @author AN515-57
 */
@Embeddable
public class FollowNoticePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "employee_id")
    private int employeeId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "employer_id")
    private int employerId;

    public FollowNoticePK() {
    }

    public FollowNoticePK(int employeeId, int employerId) {
        this.employeeId = employeeId;
        this.employerId = employerId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getEmployerId() {
        return employerId;
    }

    public void setEmployerId(int employerId) {
        this.employerId = employerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) employeeId;
        hash += (int) employerId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FollowNoticePK)) {
            return false;
        }
        FollowNoticePK other = (FollowNoticePK) object;
        if (this.employeeId != other.employeeId) {
            return false;
        }
        if (this.employerId != other.employerId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.job.pojo.FollowNoticePK[ employeeId=" + employeeId + ", employerId=" + employerId + " ]";
    }
    
}

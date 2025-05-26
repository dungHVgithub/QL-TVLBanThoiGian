/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.job.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
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
 * @author AN515-57
 */
@Entity
@Table(name = "hoso_ungtuyen")
@NamedQueries({
    @NamedQuery(name = "HosoUngtuyen.findAll", query = "SELECT h FROM HosoUngtuyen h"),
    @NamedQuery(name = "HosoUngtuyen.findByJobId", query = "SELECT h FROM HosoUngtuyen h WHERE h.hosoUngtuyenPK.jobId = :jobId"),
    @NamedQuery(name = "HosoUngtuyen.findByEmployeeId", query = "SELECT h FROM HosoUngtuyen h WHERE h.hosoUngtuyenPK.employeeId = :employeeId"),
    @NamedQuery(name = "HosoUngtuyen.findByState", query = "SELECT h FROM HosoUngtuyen h WHERE h.state = :state")})
public class HosoUngtuyen implements Serializable {

    @Size(max = 10)
    @Column(name = "state")
    private String state;
    @Lob
    @Size(max = 65535)
    @Column(name = "rv_from_employee")
    private String rvFromEmployee;
    @Lob
    @Size(max = 65535)
    @Column(name = "rv_from_employer")
    private String rvFromEmployer;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HosoUngtuyenPK hosoUngtuyenPK;
    @JoinColumn(name = "employee_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Employee employee;
    @JoinColumn(name = "job_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private JobPosting jobPosting;

    public HosoUngtuyen() {
    }

    public HosoUngtuyen(HosoUngtuyenPK hosoUngtuyenPK) {
        this.hosoUngtuyenPK = hosoUngtuyenPK;
    }

    public HosoUngtuyen(int jobId, int employeeId) {
        this.hosoUngtuyenPK = new HosoUngtuyenPK(jobId, employeeId);
    }

    public HosoUngtuyenPK getHosoUngtuyenPK() {
        return hosoUngtuyenPK;
    }

    public void setHosoUngtuyenPK(HosoUngtuyenPK hosoUngtuyenPK) {
        this.hosoUngtuyenPK = hosoUngtuyenPK;
    }


    public String getRvFromEmployee() {
        return rvFromEmployee;
    }

    public void setRvFromEmployee(String rvFromEmployee) {
        this.rvFromEmployee = rvFromEmployee;
    }

    public String getRvFromEmployer() {
        return rvFromEmployer;
    }

    public void setRvFromEmployer(String rvFromEmployer) {
        this.rvFromEmployer = rvFromEmployer;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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
        hash += (hosoUngtuyenPK != null ? hosoUngtuyenPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HosoUngtuyen)) {
            return false;
        }
        HosoUngtuyen other = (HosoUngtuyen) object;
        if ((this.hosoUngtuyenPK == null && other.hosoUngtuyenPK != null) || (this.hosoUngtuyenPK != null && !this.hosoUngtuyenPK.equals(other.hosoUngtuyenPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.job.pojo.HosoUngtuyen[ hosoUngtuyenPK=" + hosoUngtuyenPK + " ]";
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

   
    
}

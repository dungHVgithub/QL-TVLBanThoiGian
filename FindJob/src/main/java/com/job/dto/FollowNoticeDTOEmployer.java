package com.job.dto;

import java.util.Date;

public class FollowNoticeDTOEmployer {
   
    private int employeeId;
    private Date followTime;

    public FollowNoticeDTOEmployer() {
    }

    

    public FollowNoticeDTOEmployer(int employeeId, Date followTime) {
       
        this.employeeId = employeeId;
        this.followTime = followTime;
    }
    

  

    public Date getFollowTime() {
        return followTime;
    }

    public void setFollowTime(Date followTime) {
        this.followTime = followTime;
    }

    /**
     * @return the employeeId
     */
    public int getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}

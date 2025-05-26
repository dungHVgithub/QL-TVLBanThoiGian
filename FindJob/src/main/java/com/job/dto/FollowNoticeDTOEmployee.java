package com.job.dto;

import java.util.Date;

public class FollowNoticeDTOEmployee {
    private int employerId;
    private Date followTime;

    public FollowNoticeDTOEmployee() {
    }

    

    public FollowNoticeDTOEmployee(int employerId, Date followTime) {
        this.employerId = employerId;
        this.followTime = followTime;
    }
    

    public int getEmployerId() {
        return employerId;
    }

    public void setEmployerId(int employerId) {
        this.employerId = employerId;
    }

    public Date getFollowTime() {
        return followTime;
    }

    public void setFollowTime(Date followTime) {
        this.followTime = followTime;
    }
}

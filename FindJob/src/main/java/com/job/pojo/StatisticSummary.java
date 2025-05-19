package com.job.pojo;

public class StatisticSummary {
    private long totalEmployees;
    private long totalEmployers;
    private long totalJobs;
    private long pendingJobs;
    private long approvedJobs;
    private long rejectedJobs;

    public StatisticSummary(long totalEmployees, long totalEmployers, long totalJobs, long pendingJobs, long approvedJobs, long rejectedJobs) {
        this.totalEmployees = totalEmployees;
        this.totalEmployers = totalEmployers;
        this.totalJobs = totalJobs;
        this.pendingJobs = pendingJobs;
        this.approvedJobs = approvedJobs;
        this.rejectedJobs = rejectedJobs;
    }

    public long getTotalEmployees() {
        return totalEmployees;
    }

    public long getTotalEmployers() {
        return totalEmployers;
    }

    public long getTotalJobs() {
        return totalJobs;
    }

    public long getPendingJobs() {
        return pendingJobs;
    }

    public long getApprovedJobs() {
        return approvedJobs;
    }

    public long getRejectedJobs() {
        return rejectedJobs;
    }
}
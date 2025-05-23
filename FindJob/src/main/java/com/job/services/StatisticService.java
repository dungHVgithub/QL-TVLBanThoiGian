package com.job.services;

import com.job.pojo.StatisticSummary;

import java.util.List;
import java.util.Map;

public interface StatisticService {
    long getTotalEmployees();
    long getTotalEmployers();
    long getTotalJobs();
    long getJobsByState(String state);
    StatisticSummary getSummary();
    List<Map<String, Object>> getTimeSeriesData(String type, int range);


}
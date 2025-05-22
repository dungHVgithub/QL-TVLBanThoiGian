package com.job.services.impl;

import com.job.pojo.StatisticSummary;
import com.job.repositories.EmployerRepository;
import com.job.repositories.JobPostingRepository;
import com.job.repositories.UserRepository;
import com.job.services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmployerRepository employerRepository;
    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Override
    public long getTotalEmployees() {
        return userRepository.countByRole("ROLE_EMPLOYEE");
    }

    @Override
    public long getTotalEmployers() {
        return employerRepository.count();
    }

    @Override
    public long getTotalJobs() {
        return jobPostingRepository.count();
    }

    @Override
    public long getJobsByState(String state) {
        return jobPostingRepository.countByState(state.toUpperCase());
    }

    @Override
    public StatisticSummary getSummary() {
        long totalEmployees = getTotalEmployees();
        long totalEmployers = getTotalEmployers();
        long totalJobs = getTotalJobs();
        long pendingJobs = getJobsByState("PENDING");
        long approvedJobs = getJobsByState("APPROVED");
        long rejectedJobs = getJobsByState("REJECTED");
        return new StatisticSummary(totalEmployees, totalEmployers, totalJobs, pendingJobs, approvedJobs, rejectedJobs);
    }

    @Override
    public List<Map<String, Object>> getTimeSeriesData(String type, int range) {
        List<Map<String, Object>> timeSeriesData = new ArrayList<>();
        LocalDate now = LocalDate.now();

        if ("day".equalsIgnoreCase(type)) {
            // Thống kê theo ngày (range ngày gần nhất)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (int i = range - 1; i >= 0; i--) {
                LocalDate date = now.minusDays(i);
                String day = date.format(formatter);

                long employees = userRepository.countByRoleAndDate("ROLE_EMPLOYEE", day);
                long employers = employerRepository.countByDate(day);
                long jobs = jobPostingRepository.countByDate(day);

                Map<String, Object> dataPoint = new HashMap<>();
                dataPoint.put("date", day);
                dataPoint.put("employees", employees);
                dataPoint.put("employers", employers);
                dataPoint.put("jobs", jobs);
                timeSeriesData.add(dataPoint);
            }
        } else {
            // Thống kê theo tháng (range tháng gần nhất)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            for (int i = range - 1; i >= 0; i--) {
                LocalDate date = now.minusMonths(i);
                String month = date.format(formatter);

                long employees = userRepository.countByRoleAndMonth("ROLE_EMPLOYEE", month);
                long employers = employerRepository.countByMonth(month);
                long jobs = jobPostingRepository.countByMonth(month);

                Map<String, Object> dataPoint = new HashMap<>();
                dataPoint.put("month", month);
                dataPoint.put("employees", employees);
                dataPoint.put("employers", employers);
                dataPoint.put("jobs", jobs);
                timeSeriesData.add(dataPoint);
            }
        }
        return timeSeriesData;
    }
}
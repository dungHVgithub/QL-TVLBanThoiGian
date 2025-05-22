package com.job.controllers;

import com.job.pojo.StatisticSummary;
import com.job.services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiStatisticController {
    @Autowired
    private StatisticService statisticService;

    @GetMapping("/statistics")
    public ResponseEntity<StatisticSummary> getStatistics() {
        return new ResponseEntity<>(statisticService.getSummary(), HttpStatus.OK);
    }

    @GetMapping("/statistics/time-series")
    public ResponseEntity<List<Map<String, Object>>> getTimeSeriesData(
            @RequestParam(value = "type", defaultValue = "month") String type,
            @RequestParam(value = "range", defaultValue = "1") String range) {
        return new ResponseEntity<>(statisticService.getTimeSeriesData(type, Integer.parseInt(range)), HttpStatus.OK);
    }
}
package com.job.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/statistics")
public class StatisticController {

    @GetMapping
    public String showStatisticsPage() {
        return "statistics"; // Trả về tên view "statistics" (tương ứng với statistics.html)
    }
}
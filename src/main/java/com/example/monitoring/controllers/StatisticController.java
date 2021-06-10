package com.example.monitoring.controllers;

import com.example.monitoring.dao.implementations.ResourceForMonitoringRepository;
import com.example.monitoring.model.GetStatisticClass;
import com.example.monitoring.model.ResourceForMonitoring;
import com.example.monitoring.model.ResourceForMonitoringStatistic;
import com.example.monitoring.model.ResourceMonitoringResult;
import com.example.monitoring.service.StatisticOfMonitoringManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(value = "/statistic")
@Slf4j
public class StatisticController {

    private StatisticOfMonitoringManager statisticOfMonitoringManager;

    public StatisticController(StatisticOfMonitoringManager statisticOfMonitoringManager){
        this.statisticOfMonitoringManager = statisticOfMonitoringManager;
    }

    @PostMapping("/get-all-results-between-dates")
    @Async
    public List<ResourceMonitoringResult> getAllResultsBetweenDates(@RequestBody GetStatisticClass getStatisticClass) {
        return statisticOfMonitoringManager.getAllResultsBetweenDates(getStatisticClass.getId(), getStatisticClass.getFrom(), getStatisticClass.getTo());
    }

    @PostMapping("/get-statistic-between-dates")
    @Async
    public List<ResourceForMonitoringStatistic> getAllStatisticBetweenDates(@RequestBody GetStatisticClass getStatisticClass) {
        return statisticOfMonitoringManager.getStatisticBetweenDates(getStatisticClass.getId(), getStatisticClass.getFrom(), getStatisticClass.getTo());
    }

    @PostMapping("/get-statistic-for-each")
    @Async
    public List<ResourceForMonitoringStatistic> getAllStatisticForEachBetweenDates(@RequestBody GetStatisticClass getStatisticClass) {
        return statisticOfMonitoringManager.getStatisticForEachBetweenDates(getStatisticClass.getId(), getStatisticClass.getFrom(), getStatisticClass.getTo());
    }
}

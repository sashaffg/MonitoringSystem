package com.example.monitoring.dao.interfaces;

import com.example.monitoring.model.ResourceForMonitoring;
import com.example.monitoring.model.ResourceForMonitoringStatistic;
import com.example.monitoring.model.ResourceMonitoringResult;

import java.sql.Timestamp;
import java.util.List;

public interface ResultOfMonitoringRepositoryInterface {
    void addResultOfMonitoring(ResourceForMonitoring resourceForMonitoring);

    List<ResourceMonitoringResult> getAllResultsBetweenDates(int id, Timestamp from, Timestamp to);

    ResourceForMonitoringStatistic getStatisticBetweenDates(int id, Timestamp from, Timestamp to);
}

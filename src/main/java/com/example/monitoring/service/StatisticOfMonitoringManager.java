package com.example.monitoring.service;

import com.example.monitoring.dao.interfaces.ResultOfMonitoringRepositoryInterface;
import com.example.monitoring.model.ResourceForMonitoringStatistic;
import com.example.monitoring.model.ResourceMonitoringResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticOfMonitoringManager {

    private ResultOfMonitoringRepositoryInterface resultOfMonitoringRepositoryInterface;

    @Autowired
    public StatisticOfMonitoringManager(ResultOfMonitoringRepositoryInterface resultOfMonitoringRepositoryInterface){
        this.resultOfMonitoringRepositoryInterface = resultOfMonitoringRepositoryInterface;
    }


    public List<ResourceMonitoringResult> getAllResultsBetweenDates(int id, Timestamp from, Timestamp to){
        from.setHours(0);
        from.setMinutes(0);
        from.setSeconds(0);
        to.setDate(to.getDate()+1);
        to.setHours(0);
        to.setMinutes(0);
        to.setSeconds(0);
        return resultOfMonitoringRepositoryInterface.getAllResultsBetweenDates(id, from, to);
    }


    public List<ResourceForMonitoringStatistic> getStatisticBetweenDates(int id, Timestamp from, Timestamp to) {
        from.setHours(0);
        from.setMinutes(0);
        from.setSeconds(0);
        to.setDate(to.getDate()+1);
        to.setHours(0);
        to.setMinutes(0);
        to.setSeconds(0);
        ResourceForMonitoringStatistic resourceForMonitoringStatistic = resultOfMonitoringRepositoryInterface.getStatisticBetweenDates(id, from, to);
        resourceForMonitoringStatistic.setFromTimeView(from);
        resourceForMonitoringStatistic.setToTimeView(to);
        List<ResourceForMonitoringStatistic> resourceForMonitoringStatistics = new ArrayList<>();
        resourceForMonitoringStatistics.add(resourceForMonitoringStatistic);
        return resourceForMonitoringStatistics;
    }

    public List<ResourceForMonitoringStatistic> getStatisticForEachBetweenDates(int id, Timestamp from, Timestamp to) {
        from.setHours(0);
        from.setMinutes(0);
        from.setSeconds(0);
        to.setDate(to.getDate()+1);
        to.setHours(0);
        to.setMinutes(0);
        to.setSeconds(0);
        List<ResourceForMonitoringStatistic> resourceForMonitoringStatistics = new ArrayList<>();
        while(from.getDate() < to.getDate()){
            Timestamp toNextDay = new Timestamp(from.getTime());
            toNextDay.setDate(toNextDay.getDate()+1);
            ResourceForMonitoringStatistic resourceForMonitoringStatistic = resultOfMonitoringRepositoryInterface.getStatisticBetweenDates(id, from, toNextDay);
            if(resourceForMonitoringStatistic.getUrl() != null) {
                resourceForMonitoringStatistic.setFromTimeView(from);
                resourceForMonitoringStatistic.setToTimeView(toNextDay);
                resourceForMonitoringStatistics.add(resourceForMonitoringStatistic);
            }
            from.setDate(from.getDate()+1);
        }
        return resourceForMonitoringStatistics;
    }
}

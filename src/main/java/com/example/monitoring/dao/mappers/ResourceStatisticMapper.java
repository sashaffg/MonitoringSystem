package com.example.monitoring.dao.mappers;

import com.example.monitoring.model.ResourceForMonitoringStatistic;
import com.example.monitoring.model.ResourceMonitoringResult;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class ResourceStatisticMapper implements RowMapper<ResourceForMonitoringStatistic> {
    @Override
    public ResourceForMonitoringStatistic mapRow(ResultSet resultSet, int i) throws SQLException {
        ResourceForMonitoringStatistic resourceForMonitoringStatistic = new ResourceForMonitoringStatistic();
        resourceForMonitoringStatistic.setUrl(resultSet.getString("url"));
        resourceForMonitoringStatistic.setAverageResponseSize(resultSet.getInt("avg_response_size"));
        resourceForMonitoringStatistic.setAverageResponseTime(resultSet.getInt("avg_response_time"));
        resourceForMonitoringStatistic.setPercentOfSuccessfulRequests(resultSet.getInt("percentage_of_successful_requests"));
        resourceForMonitoringStatistic.setCountOfRequests(resultSet.getInt("count_of_requests"));
        resourceForMonitoringStatistic.setCountOfBadResponseCode(resultSet.getInt("count_of_bad_response_code"));
        return resourceForMonitoringStatistic;
    }
}
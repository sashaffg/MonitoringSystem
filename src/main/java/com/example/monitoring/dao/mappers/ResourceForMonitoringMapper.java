package com.example.monitoring.dao.mappers;

import com.example.monitoring.model.ResourceForMonitoring;
import com.example.monitoring.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ResourceForMonitoringMapper implements RowMapper<ResourceForMonitoring> {
    @Override
    public ResourceForMonitoring mapRow(ResultSet resultSet, int i) throws SQLException {
        ResourceForMonitoring resourceForMonitoring = new ResourceForMonitoring();
        resourceForMonitoring.setId(resultSet.getInt("resource_pk"));
        resourceForMonitoring.setUserId(resultSet.getInt("user_fk"));
        resourceForMonitoring.setUrl(resultSet.getString("url"));
        resourceForMonitoring.setRequestResendDelay(resultSet.getInt("requestResendDelay"));
        resourceForMonitoring.setActive(resultSet.getBoolean("is_active"));
        resourceForMonitoring.setMinResponseTimeForOk(resultSet.getInt("minResponseTimeForOk"));
        resourceForMonitoring.setMaxResponseTimeForOk(resultSet.getInt("maxResponseTimeForOk"));
        resourceForMonitoring.setMinResponseSize(resultSet.getInt("minResponseSize"));
        resourceForMonitoring.setMaxResponseSize(resultSet.getInt("maxResponseSize"));
        resourceForMonitoring.setExpectedResponseCode(resultSet.getInt("expectedResponseCode"));
        return resourceForMonitoring;
    }
}

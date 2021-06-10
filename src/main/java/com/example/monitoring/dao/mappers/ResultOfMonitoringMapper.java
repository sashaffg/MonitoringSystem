package com.example.monitoring.dao.mappers;

import com.example.monitoring.model.ResourceForMonitoring;
import com.example.monitoring.model.ResourceMonitoringResult;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class ResultOfMonitoringMapper implements RowMapper<ResourceMonitoringResult> {
    @Override
    public ResourceMonitoringResult mapRow(ResultSet resultSet, int i) throws SQLException {
        ResourceMonitoringResult resourceMonitoringResult = new ResourceMonitoringResult();
        resourceMonitoringResult.setUrl(resultSet.getString("url"));
        resourceMonitoringResult.setId(resultSet.getInt("resource_fk"));
        resourceMonitoringResult.setResponseTime(resultSet.getInt("response_time"));
        resourceMonitoringResult.setRequestSendTimeView(resultSet.getTimestamp("request_send_time"));
        resourceMonitoringResult.setUrlStatus(resultSet.getString("response_status"));
        resourceMonitoringResult.setResponseCodeView(String.valueOf(resultSet.getInt("response_code")));
        resourceMonitoringResult.setResponseSize(resultSet.getInt("response_size"));
        return resourceMonitoringResult;
    }
}

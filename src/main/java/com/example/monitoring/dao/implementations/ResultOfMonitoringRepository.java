package com.example.monitoring.dao.implementations;

import com.example.monitoring.dao.interfaces.ResultOfMonitoringRepositoryInterface;
import com.example.monitoring.dao.mappers.ResourceStatisticMapper;
import com.example.monitoring.dao.mappers.ResultOfMonitoringMapper;
import com.example.monitoring.exceptions.ResourceNotFoundException;
import com.example.monitoring.model.ResourceForMonitoring;
import com.example.monitoring.model.ResourceForMonitoringStatistic;
import com.example.monitoring.model.ResourceMonitoringResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PropertySource("classpath:queries/resourceForMonitoringResult.properties")
@Slf4j
@Repository
public class ResultOfMonitoringRepository implements ResultOfMonitoringRepositoryInterface {

    private final DataSource dataSource;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment env;
    private ResultOfMonitoringMapper resultOfMonitoringMapper;
    private final JdbcTemplate jdbcTemplate;
    private ResourceStatisticMapper resourceStatisticMapper;


    @Value("${addResult}")
    private String addResult;
    @Value("${getAllResultsBetweenDates}")
    private String getAllResultsBetweenDates;
    @Value("${getStatisticBetweenDates}")
    private String getStatisticBetweenDates;

    @Autowired
    public ResultOfMonitoringRepository(ResourceStatisticMapper resourceStatisticMapper, JdbcTemplate jdbcTemplate, DataSource dataSource, NamedParameterJdbcTemplate namedJdbcTemplate, Environment env,  ResultOfMonitoringMapper resultOfMonitoringMapper) {
        this.dataSource = dataSource;
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.env = env;
        this.resultOfMonitoringMapper = resultOfMonitoringMapper;
        this.jdbcTemplate = jdbcTemplate;
        this.resourceStatisticMapper = resourceStatisticMapper;
    }

    @Override
    public void addResultOfMonitoring(ResourceForMonitoring resourceForMonitoring){
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("resource_fk", resourceForMonitoring.getId());
            namedParams.put("response_time", resourceForMonitoring.getLastUrlResponse().getFirstHeader("responseTime").getValue());
            String format = "yyyy.MM.dd hh:mm:ss";
            DateFormat formatter = new SimpleDateFormat(format);
            namedParams.put("request_send_time", resourceForMonitoring.getLastRequestSendTime());
            namedParams.put("response_status", resourceForMonitoring.getUrlStatus());
            namedParams.put("response_code", resourceForMonitoring.getLastUrlResponse().getStatusLine().getStatusCode());
            namedParams.put("response_size", resourceForMonitoring.getResponseSize());
            namedJdbcTemplate.update(addResult, namedParams);

    }

    @Override
    public List<ResourceMonitoringResult> getAllResultsBetweenDates(int id, Timestamp from, Timestamp to){
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("resource_fk", id);
        namedParameters.addValue("from", from);
        namedParameters.addValue("to", to);
        return namedJdbcTemplate.query(getAllResultsBetweenDates, namedParameters, resultOfMonitoringMapper);

    }
    @Override
    public ResourceForMonitoringStatistic getStatisticBetweenDates(int id, Timestamp from, Timestamp to) {
        try {
            Map<String, Object> namedParameters = new HashMap<>();
            namedParameters.put("resource_fk", id);
            namedParameters.put("from", from);
            namedParameters.put("to", to);
            return namedJdbcTemplate.queryForObject(getStatisticBetweenDates, namedParameters, resourceStatisticMapper);
        }catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Statistic resource [" + id + "] not found ");
        }
    }
}

package com.example.monitoring.dao.implementations;

import com.example.monitoring.dao.interfaces.ResourceForMonitoringRepositoryInterface;
import com.example.monitoring.dao.mappers.ResourceForMonitoringMapper;
import com.example.monitoring.exceptions.ResourceNotFoundException;
import com.example.monitoring.model.ResourceForMonitoring;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@PropertySource("classpath:queries/resourceForMonitoring.properties")
@Slf4j
@Repository
public class ResourceForMonitoringRepository implements ResourceForMonitoringRepositoryInterface {

    public static List<ResourceForMonitoring> inMemoryResources = new LinkedList();
    private final DataSource dataSource;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment env;
    private ResourceForMonitoringMapper resourceForMonitoringMapper;
    private final JdbcTemplate jdbcTemplate;

    @Value("${removeResource}")
    private String removeResource;
    @Value("${addResource}")
    private String addResource;
    @Value("${findById}")
    private String findById;
    @Value("${setIsActive}")
    private String setIsActive;
    @Value("${getAllResources}")
    private String getAllResources;
    @Value("${removeAllResourceResults}")
    private String removeAllResourceResults;

    @Autowired
    public ResourceForMonitoringRepository(JdbcTemplate jdbcTemplate, DataSource dataSource, NamedParameterJdbcTemplate namedJdbcTemplate, Environment env,  ResourceForMonitoringMapper resourceForMonitoringMapper) {
        this.dataSource = dataSource;
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.env = env;
        this.resourceForMonitoringMapper = resourceForMonitoringMapper;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void removeResourceForMonitoring(int id) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("resource_pk", id);
        inMemoryResources.removeIf(rfm -> rfm.getId() == id);
        namedJdbcTemplate.update(removeAllResourceResults, namedParams);
        namedJdbcTemplate.update(removeResource, namedParams);
    }

    @Override
    public void addResourceForMonitoring(ResourceForMonitoring resourceForMonitoring) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("user_fk", resourceForMonitoring.getUserId());
        namedParams.put("url", resourceForMonitoring.getUrl());
        namedParams.put("is_active", resourceForMonitoring.isActive());
        namedParams.put("minResponseTimeForOk", resourceForMonitoring.getMinResponseTimeForOk());
        namedParams.put("maxResponseTimeForOk", resourceForMonitoring.getMaxResponseTimeForOk());
        namedParams.put("minResponseSize", resourceForMonitoring.getMinResponseSize());
        namedParams.put("requestResendDelay", resourceForMonitoring.getRequestResendDelay());
        namedParams.put("maxResponseSize", resourceForMonitoring.getMaxResponseSize());
        namedParams.put("expectedResponseCode", resourceForMonitoring.getExpectedResponseCode());
        namedJdbcTemplate.update(addResource, namedParams);
        resourceForMonitoring.setRequestResendTime();
        List<ResourceForMonitoring> resourcesForMonitoring = getAllResourceForMonitoring();
        for(ResourceForMonitoring rfm : resourcesForMonitoring){
            if(!inMemoryResources.contains(rfm) ){
                inMemoryResources.add(rfm);
            }
        }
    }

    @Override
    public ResourceForMonitoring findResourceForMonitoringById(int id) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("resource_pk", id);
            return namedJdbcTemplate.queryForObject(findById, namedParams, resourceForMonitoringMapper);
        }catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Resource [" + id + "] not found ");
        }
    }

    @Override
    public List<ResourceForMonitoring> getAllResourceForMonitoring() {
        return jdbcTemplate.query(getAllResources, resourceForMonitoringMapper);
    }

    @Override
    public void setResourceActivity(int id) {
            Map<String, Object> namedParams = new HashMap<>();
            for(ResourceForMonitoring r : inMemoryResources){
                if(r.getId() == id){
                    r.setActive(!r.isActive());
                    namedParams.put("is_active", r.isActive());
                }
            }
            namedParams.put("resource_pk", id);
            namedJdbcTemplate.update(setIsActive, namedParams);

    }
}

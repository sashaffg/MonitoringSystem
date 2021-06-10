package com.example.monitoring.dao.interfaces;

import com.example.monitoring.model.ResourceForMonitoring;

import java.util.List;

public interface ResourceForMonitoringRepositoryInterface {
    void removeResourceForMonitoring(int id);

    void addResourceForMonitoring(ResourceForMonitoring resourceForMonitoring);

    ResourceForMonitoring findResourceForMonitoringById(int id);

    List<ResourceForMonitoring> getAllResourceForMonitoring();

    void setResourceActivity(int id);
}

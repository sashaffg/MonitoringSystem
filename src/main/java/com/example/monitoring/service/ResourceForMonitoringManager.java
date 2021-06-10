package com.example.monitoring.service;

import com.example.monitoring.dao.implementations.ResourceForMonitoringRepository;
import com.example.monitoring.dao.interfaces.ResultOfMonitoringRepositoryInterface;
import com.example.monitoring.dao.interfaces.ResourceForMonitoringRepositoryInterface;
import com.example.monitoring.model.ResourceForMonitoring;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Timer;

@Service
public class ResourceForMonitoringManager {

    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    ResourceForMonitoringRepositoryInterface resourceForMonitoringRepository;
    ResultOfMonitoringRepositoryInterface resultOfMonitoringRepositoryInterface;

    @Autowired
    public ResourceForMonitoringManager(ResourceForMonitoringRepositoryInterface resourceForMonitoringRepository,  ResultOfMonitoringRepositoryInterface resultOfMonitoringRepositoryInterface){
        this.resourceForMonitoringRepository = resourceForMonitoringRepository;
        this.resultOfMonitoringRepositoryInterface = resultOfMonitoringRepositoryInterface;

    }

    Timer time = new Timer();

    @PostConstruct
    public void startCheckingService() {
        ResourceForMonitoringRepository.inMemoryResources = resourceForMonitoringRepository.getAllResourceForMonitoring();

        MonitoringScheduledTask mst = new MonitoringScheduledTask(resultOfMonitoringRepositoryInterface);
        time.schedule(mst, 0, 1000);
    }


    public void changeActivity(int id)  {
        resourceForMonitoringRepository.setResourceActivity(id);
    }


    public void addNewResourceForMonitoring(ResourceForMonitoring resourceForMonitoring) {
        resourceForMonitoringRepository.addResourceForMonitoring(resourceForMonitoring);
    }


    public void removeResourceForMonitoring(int id){
        resourceForMonitoringRepository.removeResourceForMonitoring(id);
    }


}

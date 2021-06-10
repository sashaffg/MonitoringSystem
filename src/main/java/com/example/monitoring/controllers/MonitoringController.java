package com.example.monitoring.controllers;

import com.example.monitoring.dao.implementations.ResourceForMonitoringRepository;
import com.example.monitoring.dao.implementations.ResultOfMonitoringRepository;
import com.example.monitoring.model.ResourceForMonitoring;
import com.example.monitoring.model.ResourceMonitoringResult;
import com.example.monitoring.service.ResourceForMonitoringManager;
import com.example.monitoring.service.ResourceMonitoringInMemoryThread;
import com.example.monitoring.service.ResourceMonitoringThread;
import com.example.monitoring.service.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/monitoring-service")
@Slf4j
public class MonitoringController {
    private ResourceForMonitoringManager resourceForMonitoringManager;
    private UserManager userManager;
    private ResultOfMonitoringRepository resultOfMonitoringRepository;

    @Autowired
    public MonitoringController(ResourceForMonitoringManager resourceForMonitoringManager, UserManager userManager, ResultOfMonitoringRepository resultOfMonitoringRepository){
        this.resourceForMonitoringManager = resourceForMonitoringManager;
        this.userManager = userManager;
        this.resultOfMonitoringRepository = resultOfMonitoringRepository;
    }

    @PostMapping("/add_resource")
    @Async
    public ResponseEntity<Map> addResource(@RequestBody ResourceForMonitoring resourceForMonitoring) {
        int user_id = userManager.getCurrentUserId();
        resourceForMonitoring.setUserId(user_id);
        resourceForMonitoringManager.addNewResourceForMonitoring(resourceForMonitoring);
        Map<Object, Object> response = new HashMap<>();
        response.put("msg", "Activity status changed");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get_all_results")
    @Async
    public List<ResourceMonitoringResult> getAll() {
        int user_id = userManager.getCurrentUserId();
        List<ResourceMonitoringResult> resourceMonitoringResultList = new LinkedList<>();
        for (ResourceForMonitoring resource : ResourceForMonitoringRepository.inMemoryResources) {
            if(resource.getUserId()==user_id && resource.getLastUrlResponse() != null){
                ResourceMonitoringResult rmr = new ResourceMonitoringResult();
                rmr.setResource_id(resource.getId());
                rmr.setUrlStatus(resource.getUrlStatus());
                rmr.setResponseSize(resource.getResponseSize());
                rmr.setResponseCodeView(resource.getResponseCode());
                rmr.setResponseTime(Integer.parseInt(resource.getResponseTime()));
                rmr.setRequestSendTimeView(resource.getLastRequestSendTime());
                rmr.setActive(resource.isActive());
                rmr.setUrl(resource.getUrl());
                resourceMonitoringResultList.add(rmr);
            }else if(resource.getUserId()==user_id && resource.getLastUrlResponse() == null) {
                ResourceMonitoringResult rmr = new ResourceMonitoringResult();
                rmr.setResource_id(resource.getId());
                rmr.setUrlStatus("-");
                rmr.setResponseSize(0);
                rmr.setResponseCodeView("-");
                rmr.setResponseTime(0);
                rmr.setRequestSendTimeView(resource.getLastRequestSendTime());
                rmr.setActive(resource.isActive());
                rmr.setUrl(resource.getUrl());
                resourceMonitoringResultList.add(rmr);
            }

        }
        return resourceMonitoringResultList;
    }

    @PostMapping("/change_activity")
    @Async
    public ResponseEntity<Map> changeActivity(@RequestBody ResourceMonitoringResult resourceForMonitoringResult) {
        resourceForMonitoringManager.changeActivity(resourceForMonitoringResult.getResource_id());
        Map<Object, Object> response = new HashMap<>();
        response.put("msg", "Activity status changed");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/remove_resource")
    @Async
    public ResponseEntity<Map> removeResource(@RequestBody ResourceMonitoringResult resourceForMonitoringResult) {
        resourceForMonitoringManager.removeResourceForMonitoring(resourceForMonitoringResult.getResource_id());
        Map<Object, Object> response = new HashMap<>();
        response.put("msg", "Resource removed");
        return ResponseEntity.ok(response);
    }

}

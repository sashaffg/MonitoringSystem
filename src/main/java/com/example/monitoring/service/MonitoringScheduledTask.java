/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.monitoring.service;

import com.example.monitoring.dao.implementations.ResourceForMonitoringRepository;
import com.example.monitoring.dao.interfaces.ResultOfMonitoringRepositoryInterface;
import com.example.monitoring.model.ResourceForMonitoring;

import java.util.Date;
import java.util.TimerTask;

public class MonitoringScheduledTask extends TimerTask {

    ResultOfMonitoringRepositoryInterface resultOfMonitoringRepositoryInterface;

    public MonitoringScheduledTask( ResultOfMonitoringRepositoryInterface resultOfMonitoringRepositoryInterface){
        this.resultOfMonitoringRepositoryInterface = resultOfMonitoringRepositoryInterface;
    }

    @Override
    public void run() {
        for (ResourceForMonitoring resForMonitoring : ResourceForMonitoringRepository.inMemoryResources) {
            if ((resForMonitoring.getRequestResendTime().before(new Date())
                    || resForMonitoring.getRequestResendTime().equals(new Date()))
                    && resForMonitoring.isActive()) {
                ResourceMonitoringThread resourceMonitoringThread = new ResourceMonitoringThread(resForMonitoring, resultOfMonitoringRepositoryInterface);
                resourceMonitoringThread.start();

            }
        }
    }
}

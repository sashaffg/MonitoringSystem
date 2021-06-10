/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.monitoring.service;

import com.example.monitoring.dao.interfaces.ResultOfMonitoringRepositoryInterface;
import com.example.monitoring.model.ResourceForMonitoring;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResourceMonitoringThread extends Thread {

    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    ResourceForMonitoring resourceForMonitoring;
    ResultOfMonitoringRepositoryInterface resultOfMonitoringRepositoryInterface;


    public ResourceMonitoringThread(ResourceForMonitoring resourceForMonitoring, ResultOfMonitoringRepositoryInterface resultOfMonitoringRepositoryInterface) {
        this.resourceForMonitoring = resourceForMonitoring;
        this.resultOfMonitoringRepositoryInterface = resultOfMonitoringRepositoryInterface;
    }

    public CloseableHttpResponse getResponse(String url) throws IOException {
        HttpGet requestTo = new HttpGet(url);
        requestTo.addHeader("custom-key", "mkyong");
        requestTo.addHeader(HttpHeaders.USER_AGENT, "Googlebot");

        Long startTime = new Date().getTime();
        CloseableHttpResponse responseFromUrl = httpClient.execute(requestTo);
        Long endTime = new Date().getTime();

        Long resTime = endTime - startTime;
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
        responseFromUrl.addHeader("requestSendTime", formatForDateNow.format(startTime));
        resourceForMonitoring.setLastRequestSendTime(new Timestamp(startTime));
        responseFromUrl.addHeader("responseTime", resTime.toString());
        return responseFromUrl;

    }

    @Override
    public void run() {
        try {
            resourceForMonitoring.setLastUrlResponse(getResponse(resourceForMonitoring.getUrl()));
            resourceForMonitoring.setRequestResendTime();
            resultOfMonitoringRepositoryInterface.addResultOfMonitoring(resourceForMonitoring);
            this.interrupt();
        } catch (IOException ex) {
            Logger.getLogger(ResourceMonitoringThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

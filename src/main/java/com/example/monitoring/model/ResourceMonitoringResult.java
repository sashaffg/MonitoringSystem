package com.example.monitoring.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class ResourceMonitoringResult {
    private int id;

    private String responseCodeView;
    private String url;
    private boolean isActive;
    private String requestSendTimeView;

    private int resource_id;
    private Timestamp requestSendTime;
    private int responseTime;
    private String urlStatus;
    private int responseCode;
    private long responseSize;

    public void setRequestSendTimeView(Timestamp requestSendTime){
        if(requestSendTime == null){
            requestSendTimeView = "-";
            return;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' HH:mm:ss");
        requestSendTimeView = requestSendTime.toLocalDateTime().format(dateTimeFormatter);
    }
}

package com.example.monitoring.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class ResourceForMonitoringStatistic {
    private String Url;
    private Timestamp fromTime;
    private Timestamp toTime;
    private String fromTimeView;
    private String toTimeView;
    private int averageResponseTime;
    private int averageResponseSize;
    private int percentOfSuccessfulRequests;
    private int countOfRequests;
    private int countOfBadResponseCode;

    public void setFromTimeView(Timestamp fromTime){
        if(fromTime == null){
            fromTimeView = "-";
            return;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        fromTimeView = fromTime.toLocalDateTime().format(dateTimeFormatter);
    }
    public void setToTimeView(Timestamp toTime){
        if(toTime == null){
            toTimeView = "-";
            return;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        toTimeView = toTime.toLocalDateTime().format(dateTimeFormatter);
    }
}

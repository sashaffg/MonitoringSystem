package com.example.monitoring.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class GetStatisticClass {
    private  int id;
    private  Timestamp from;
    private  Timestamp to;
}

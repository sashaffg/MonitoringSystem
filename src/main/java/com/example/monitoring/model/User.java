package com.example.monitoring.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class User {
    private int id;
    @JsonProperty("email")
    @JSONField(name="email")
    private String email;
    @JsonProperty("login")
    @JSONField(name="login")
    private String login;
    @JsonProperty("password")
    @JSONField(name="password")
    private String password;
    private Role role = Role.ROLE_CLIENT;
    private Timestamp refreshDate;

}

package com.example.monitoring.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
public class VerificationToken {
    private int id;
    private String token;
    private String email;
    private String login;
    private String password;

    public VerificationToken(User user) {
        this.email = user.getEmail();
        this.login = user.getLogin();
        this.password = user.getPassword();
        token = UUID.randomUUID().toString();
    }
}

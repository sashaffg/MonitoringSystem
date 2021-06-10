package com.example.monitoring.dao.interfaces;

import com.example.monitoring.model.User;

import java.util.Date;

public interface UserRepositoryInterface {
    void addUser(User user);


    User findByLogin(String login);

    void removeUser(String login);

    User findById(int id);

    Boolean isExistByLogin(String login);

    Boolean isExistByMail(String email);
}

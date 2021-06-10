package com.example.monitoring.service;

import java.util.Date;

import com.example.monitoring.dao.interfaces.UserRepositoryInterface;
import com.example.monitoring.exceptions.EmailExistException;
import com.example.monitoring.exceptions.LoginExistException;
import com.example.monitoring.model.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Data
@Service
@Slf4j
public class UserManager {
    private UserRepositoryInterface userRepositoryInterface;
    private VerificationTokenManager verificationTokenManager;
    private EmailSender emailSender;
    @Autowired
    public UserManager(UserRepositoryInterface userRepositoryInterface,
                       EmailSender emailSender,
                       VerificationTokenManager verificationTokenManager) {
        this.userRepositoryInterface = userRepositoryInterface;
        this.emailSender = emailSender;
        this.verificationTokenManager = verificationTokenManager;
    }


    public int getUserIdByLogin(String login) {
           return userRepositoryInterface.findByLogin(login).getId();
    }
    public void removeUserByLogin(String login) {
        userRepositoryInterface.removeUser(login);
    }

    public void addUser(User user) {
        userRepositoryInterface.addUser(user);
    }

    public void activateUser(String token) {
        User user = verificationTokenManager.activateToken(token);
        userRepositoryInterface.addUser(user);
        verificationTokenManager.removeVerificationToken(token);
    }

    public User getUserById(int id) {
        return userRepositoryInterface.findById(id);
    }

    public Boolean isExistByLogin(String login) {
        return userRepositoryInterface.isExistByLogin(login);
    }

    public Boolean isExistByMail(String mail) {
        return userRepositoryInterface.isExistByMail(mail);
    }

    public User getUserByLogin(String login) {
        return userRepositoryInterface.findByLogin(login);
    }



    public void register(User user) {
        if (isExistByLogin(user.getLogin())){
            throw new LoginExistException("Login is already in use");
        }
        if (isExistByMail(user.getEmail())){
            throw new EmailExistException("Email is already in use");
        }
        VerificationToken verificationToken = new VerificationToken(user);
        verificationTokenManager.addToken(verificationToken);

        String message = "To verification your account, please click here : "
                + "http://localhost:8080/user-service/verification/user?token="
                + verificationToken.getToken();
        log.info("Verification link [ {} ]", message);
        emailSender.sendMessage(user.getEmail(), "Complete Registration!", message);
    }


    public String getCurrentUserLogin(){
        return ((UserDetails) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal()).getUsername();
    }

    public int getCurrentUserId(){
        return userRepositoryInterface.findByLogin(getCurrentUserLogin()).getId();
    }
}

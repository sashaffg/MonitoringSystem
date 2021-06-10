package com.example.monitoring.service;

import com.example.monitoring.dao.interfaces.VerificationTokenRepositoryInterface;
import com.example.monitoring.model.User;
import com.example.monitoring.model.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationTokenManager {
    private VerificationTokenRepositoryInterface verificationTokenRepositoryInterface;
    @Autowired
    public VerificationTokenManager(VerificationTokenRepositoryInterface verificationTokenRepositoryInterface) {
        this.verificationTokenRepositoryInterface = verificationTokenRepositoryInterface;
    }
    
    public VerificationToken findVerificationToken(String verificationToken) {
        return verificationTokenRepositoryInterface.findByToken(verificationToken);
    }
    
    public void removeVerificationToken(String verificationToken) {
        verificationTokenRepositoryInterface.removeVerificationToken(verificationToken);
    }

    public User activateToken(String verificationTokenString) {
        VerificationToken verificationToken = verificationTokenRepositoryInterface.findByToken(verificationTokenString);
        User user = new User();
        user.setEmail(verificationToken.getEmail());
        user.setPassword(verificationToken.getPassword());
        user.setLogin(verificationToken.getLogin());
        return user;
    }
    
    public void addToken(VerificationToken verificationToken)
    {
       verificationTokenRepositoryInterface.addToken(verificationToken);
    }
}

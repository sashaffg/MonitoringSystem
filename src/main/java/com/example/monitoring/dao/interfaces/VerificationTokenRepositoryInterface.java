package com.example.monitoring.dao.interfaces;

import com.example.monitoring.model.VerificationToken;

public interface VerificationTokenRepositoryInterface {
    VerificationToken findByToken(String login);

    void addToken(VerificationToken verificationToken);

    void removeVerificationToken(String token);
}

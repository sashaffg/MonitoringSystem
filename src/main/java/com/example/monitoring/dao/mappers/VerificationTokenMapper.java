package com.example.monitoring.dao.mappers;

import com.example.monitoring.model.User;
import com.example.monitoring.model.VerificationToken;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class VerificationTokenMapper implements RowMapper<VerificationToken> {
    @Override
    public VerificationToken mapRow(ResultSet resultSet, int i) throws SQLException {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setId(resultSet.getInt("token_pk"));
        verificationToken.setToken(resultSet.getString("token"));
        verificationToken.setLogin(resultSet.getString("login"));
        verificationToken.setPassword(resultSet.getString("password"));
        verificationToken.setEmail(resultSet.getString("email"));
        return verificationToken;
    }
}
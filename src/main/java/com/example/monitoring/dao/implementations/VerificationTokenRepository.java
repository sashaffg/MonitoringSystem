package com.example.monitoring.dao.implementations;

import com.example.monitoring.dao.interfaces.VerificationTokenRepositoryInterface;
import com.example.monitoring.dao.mappers.VerificationTokenMapper;
import com.example.monitoring.exceptions.UserNotFoundException;
import com.example.monitoring.model.VerificationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@PropertySource("classpath:queries/verificationToken.properties")
@Slf4j
@Repository
public class VerificationTokenRepository implements VerificationTokenRepositoryInterface {

    private final DataSource dataSource;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment env;
    VerificationTokenMapper verificationTokenMapper;

    @Value("${findByToken}")
    private String findByToken;
    @Value("${addToken}")
    private String addToken;
    @Value("${removeToken}")
    private String removeToken;

    @Autowired
    public VerificationTokenRepository(DataSource dataSource, NamedParameterJdbcTemplate namedJdbcTemplate, Environment env,  VerificationTokenMapper verificationTokenMapper) {
        this.dataSource = dataSource;
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.env = env;
        this.verificationTokenMapper = verificationTokenMapper;
    }

    @Override
    public VerificationToken findByToken(String login) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("token", login);
            return namedJdbcTemplate.queryForObject(findByToken, namedParams, verificationTokenMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("User [" + login + "] not found ");
        }
    }

    @Override
    public void addToken(VerificationToken verificationToken) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("token", verificationToken.getToken());
        namedParams.put("login", verificationToken.getLogin());
        namedParams.put("password", verificationToken.getPassword());
        namedParams.put("email", verificationToken.getEmail());
        namedJdbcTemplate.update(addToken, namedParams);
    }

    @Override
    public void removeVerificationToken(String token) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("token", token);
        namedJdbcTemplate.update(removeToken, namedParams);
    }
}

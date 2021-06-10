package com.example.monitoring.dao.implementations;



import com.example.monitoring.dao.interfaces.UserRepositoryInterface;
import com.example.monitoring.dao.mappers.UserMapper;
import com.example.monitoring.exceptions.UserNotFoundException;
import com.example.monitoring.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@PropertySource("classpath:queries/user.properties")
@Slf4j
@Repository
public class UserRepository implements UserRepositoryInterface {
    private final DataSource dataSource;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment env;
    UserMapper userMapper;

    @Value("${findByLogin}")
    private String findByLogin;
    @Value("${findById}")
    private String findById;
    @Value("${findByEmail}")
    private String findByEmail;
    @Value("${addUser}")
    private String addUser;
    @Value("${setRefreshDate}")
    private String setRefreshDate;
    @Value("${removeUser}")
    private String removeUser;

    @Autowired
    public UserRepository(DataSource dataSource, NamedParameterJdbcTemplate namedJdbcTemplate,
                          Environment env,  UserMapper userMapper) {
        this.dataSource = dataSource;
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.env = env;
        this.userMapper = userMapper;
    }

    @Override
    public void addUser(User user) {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("login", user.getLogin());
            namedParams.put("password", user.getPassword());
            namedParams.put("email", user.getEmail());
            namedJdbcTemplate.update(addUser, namedParams);
    }

    @Override
    public User findByLogin(String login) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("login", login);
            return namedJdbcTemplate.queryForObject(findByLogin, namedParams,  userMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("User [" + login + "] not found ");
        }
    }

    @Override
    public void removeUser(String login) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("login", login);
        namedJdbcTemplate.update(removeUser, namedParams);
    }

    @Override
    public User findById(int id) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_pk", id);
            return namedJdbcTemplate.queryForObject(findById, namedParams, userMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("User [" + id + "] not found ");
        }
    }

    @Override
    public Boolean isExistByLogin(String login) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("login", login);
            namedJdbcTemplate.queryForObject(findByLogin, namedParams, userMapper);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
    @Override
    public Boolean isExistByMail(String email) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("email", email);
            namedJdbcTemplate.queryForObject(findByEmail, namedParams, userMapper);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}

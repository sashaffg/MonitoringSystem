package com.example.monitoring.controllers;


import com.example.monitoring.dao.implementations.ResourceForMonitoringRepository;
import com.example.monitoring.dao.implementations.UserRepository;
import com.example.monitoring.model.ResourceForMonitoring;
import com.example.monitoring.model.Role;
import com.example.monitoring.model.User;
import com.example.monitoring.model.VerificationToken;
import com.example.monitoring.security.JwtProvider;
import com.example.monitoring.service.UserManager;
import com.example.monitoring.service.VerificationTokenManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/user-service")
@Slf4j
public class AuthenticationController {
    private UserManager userManager;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;

    @Autowired
    public AuthenticationController(JwtProvider jwtProvider, AuthenticationManager authenticationManager,
                                    PasswordEncoder passwordEncoder,UserManager userManager) {
        this.passwordEncoder = passwordEncoder;
        this.userManager = userManager;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/signup")
    @Async
    public ResponseEntity<Map> register(@RequestBody User user){
        log.info("POST /register/user [{}, {}]", user.getLogin(), user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userManager.register(user);
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/verification/user")
    @Async
    public String confirmUserAccount(@RequestParam("token") String verificationToken) {
        log.info("PUT /verification/user");
        userManager.activateUser(verificationToken);
        return "<head>\n" +
                "<meta http-equiv=\"refresh\" content=\"1;URL=http://localhost:3000/\" />\n" +
                "</head>";
    }

    @PostMapping("/signin")
    @Async
    public ResponseEntity<Map> signin(@RequestBody User user) {
        log.info("POST /signin [{}]", user.getLogin());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword()));
        Map<Object, Object> response = new HashMap<>();
        response.put("token", jwtProvider.createToken(user.getLogin(), Role.ROLE_CLIENT));
        response.put("username", user.getLogin());
        return ResponseEntity.ok(response);
    }



}

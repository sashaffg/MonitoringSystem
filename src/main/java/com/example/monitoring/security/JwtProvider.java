package com.example.monitoring.security;

import com.example.monitoring.exceptions.TokenValidationException;
import com.example.monitoring.model.Role;
import com.example.monitoring.model.User;
import com.example.monitoring.service.UserManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {

    @Value("${jwt.token.secret}")
    private String secretKey;

    @Value("${jwt.token.expired}")
    private long validityTime;

    @Value("${jwt.token.secondPause}")
    private long secondPause;

    private JwtUserDetails JwtUserDetails;
    private UserManager userManager;
    @Autowired
    public JwtProvider(com.example.monitoring.security.JwtUserDetails jwtUserDetails, UserManager userManager) {
        JwtUserDetails = jwtUserDetails;
        this.userManager = userManager;
    }
    public JwtProvider(){}

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String login, Role role) {
        User user = userManager.getUserByLogin(login);
        Claims claims = Jwts.claims().setSubject(login);
        claims.put("role", role);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityTime);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = JwtUserDetails.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer_")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            final User user = userManager.getUserByLogin(claims.getSubject());
            return true;
        } catch (Exception e) {
            log.warn("Validation token error ");
            throw new TokenValidationException("Expired or invalid JWT token", HttpStatus.UNAUTHORIZED);
        }
    }

}

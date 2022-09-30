package com.testsigma.api_bed_test.service;


import com.testsigma.api_bed_test.config.ApplicationConfig;
import com.testsigma.api_bed_test.security.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtTokenGeneratorService {
    public static final long  TOKEN_EXPIRY_TIME = 5 * 60 * 1000;
    @Autowired
    private final ApplicationConfig applicationConfig;


    public  String generateSessionToken(User user) {
        Claims claims = Jwts.claims();
        claims.setSubject(user.getSessionId());
        claims.put("email", user.getEmail());
        claims.put("picture", user.getPicture());
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512,applicationConfig.getJwtSecret())
                .compact();
    }

    public String generateWebAppLoginToken(String userEmail, String domain, Integer tenantId) {
        long currentTime = System.currentTimeMillis();
        Claims claims = Jwts.claims().setSubject(userEmail);
        claims.put("domain", domain);
        claims.put("tenantId", tenantId + "");
        claims.put("authenticationType", "ADMIN_SSO");
        claims.put("issued_at", currentTime);
        claims.put("email", userEmail);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("Testsigma")
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentTime + TOKEN_EXPIRY_TIME))
                .signWith(SignatureAlgorithm.HS512, applicationConfig.getJwtSecret())
                .compact();
    }

    public User parseSessionId(String token) {
        try {
            User user = new User();
            Claims body = Jwts.parser()
                .setSigningKey(applicationConfig.getJwtSecret())
                .parseClaimsJws(token)
                .getBody();
            user.setEmail(body.get("email").toString());
            user.setPicture(body.get("picture").toString());
            user.setSessionId(body.getSubject());
            return user;
        } catch (JwtException | ClassCastException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}

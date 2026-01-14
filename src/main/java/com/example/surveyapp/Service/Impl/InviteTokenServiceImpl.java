package com.example.surveyapp.Service.Impl;

import com.example.surveyapp.Service.IInviteTokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Service
public class InviteTokenServiceImpl implements IInviteTokenService {

    @Value("${survey.invite.secret}")
    private String secret;

    @Value("${survey.invite.expiration-minutes}")
    private long expirationMinutes;

    public String generateToken(String email, String surveyId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMinutes * 60 * 1000);

        return Jwts.builder()
                .setSubject(email)
                .claim("surveyId", surveyId)
                .claim("scope", "SURVEY_INVITE")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .setId(UUID.randomUUID().toString())
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}

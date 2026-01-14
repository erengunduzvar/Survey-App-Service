package com.example.surveyapp.Validators;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;


    @Service
    public class InviteTokenValidator {

        @Value("${survey.invite.secret}")
        private String secret;

        public Claims validate(String token) {
            return Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }
    }



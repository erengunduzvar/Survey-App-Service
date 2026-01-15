package com.example.surveyapp.Service;

public interface InviteTokenService {
    String generateToken(String email, String surveyId);
}

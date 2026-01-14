package com.example.surveyapp.Service;

public interface IInviteTokenService {
    String generateToken(String email, String surveyId);
}

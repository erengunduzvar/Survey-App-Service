package com.example.surveyapp.Service;

import com.example.surveyapp.Dto.LoginRequest;
import com.example.surveyapp.Dto.LoginResponseDto;
import com.example.surveyapp.Entity.UserAccount;

public interface AuthService {
    public LoginResponseDto authenticate(LoginRequest req);
    public LoginResponseDto register(UserAccount user);
}

package com.example.surveyapp.Service;

import com.example.surveyapp.Model.Auth.LoginRequest;
import com.example.surveyapp.Model.Dto.LoginResponseDto;
import com.example.surveyapp.Model.Entity.UserAccount;

public interface AuthService {
    public LoginResponseDto authenticate(LoginRequest req);
    public LoginResponseDto register(UserAccount user);
}

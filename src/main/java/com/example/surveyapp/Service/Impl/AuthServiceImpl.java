package com.example.surveyapp.Service.Impl;

import com.example.surveyapp.Model.Auth.LoginRequest;
import com.example.surveyapp.Model.Dto.LoginResponseDto;
import com.example.surveyapp.Model.Entity.UserAccount;
import com.example.surveyapp.Repository.UserAccountRepository;
import com.example.surveyapp.Security.JwtService;
import com.example.surveyapp.Service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserAccountRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    @Override
    public LoginResponseDto authenticate(LoginRequest req) {
        UserAccount user = userRepo.findByEmail(req.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        String token = jwtService.generateToken(user);

        return new LoginResponseDto(user.getId(), user.getEmail(), user.getRole(), token);
    }

    @Transactional
    public LoginResponseDto register(UserAccount user) {
        user.setPassword(encoder.encode(user.getPassword()));

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }

        UserAccount saved = userRepo.save(user);

        String token = jwtService.generateToken(saved);

        return new LoginResponseDto(saved.getId(), saved.getEmail(), saved.getRole(), token);
    }
}

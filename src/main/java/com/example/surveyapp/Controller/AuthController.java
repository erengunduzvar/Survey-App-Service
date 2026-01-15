package com.example.surveyapp.Controller;

import com.example.surveyapp.Dto.LoginRequest;

import com.example.surveyapp.Dto.LoginResponseDto;
import com.example.surveyapp.Entity.UserAccount;
import com.example.surveyapp.Repository.UserAccountRepository;
import com.example.surveyapp.Security.JwtService;
import com.example.surveyapp.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserAccountRepository userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.authenticate(req));
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDto> register(@RequestBody UserAccount user) {
        // Register mantığını da AuthService içine taşıman iyi olur
        return ResponseEntity.ok(authService.register(user));
    }

}

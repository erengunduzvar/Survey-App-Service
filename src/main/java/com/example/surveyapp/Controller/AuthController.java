package com.example.surveyapp.Controller;

import com.example.surveyapp.Model.Auth.LoginRequest;

import com.example.surveyapp.Model.Dto.LoginResponseDto;
import com.example.surveyapp.Model.Entity.UserAccount;
import com.example.surveyapp.Repository.UserAccountRepository;
import com.example.surveyapp.Security.JwtService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/register")
    public LoginResponseDto register(@RequestBody UserAccount user) {
        user.setPassword(encoder.encode(user.getPassword()));
        if (user.getRole() == null) user.setRole("ROLE_USER");
        UserAccount saved = userRepo.save(user);
        String token = jwtService.generateToken(saved);
        return new LoginResponseDto(saved.getId(), saved.getEmail(), saved.getRole(), token);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequest req) {
        UserAccount user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }
        String token = jwtService.generateToken(user);
        return new LoginResponseDto(user.getId(), user.getEmail(), user.getRole(), token);
    }
}

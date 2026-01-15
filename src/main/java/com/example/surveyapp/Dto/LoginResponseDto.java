package com.example.surveyapp.Dto;

public record LoginResponseDto(
        Long id,
        String email,
        String role,
        String token
) {}


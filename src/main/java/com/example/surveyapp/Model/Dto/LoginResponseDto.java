package com.example.surveyapp.Model.Dto;

public record LoginResponseDto(
        Long id,
        String email,
        String role,
        String token
) {}


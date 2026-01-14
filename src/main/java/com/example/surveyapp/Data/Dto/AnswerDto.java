package com.example.surveyapp.Data.Dto;

public record AnswerDto(
        Long id,
        String answer,
        Long questionId,
        String surveyId
) {}

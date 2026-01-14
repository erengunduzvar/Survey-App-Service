package com.example.surveyapp.Model.Dto;

public record AnswerDto(
        Long id,
        String answer,
        Long questionId,
        String surveyId
) {}

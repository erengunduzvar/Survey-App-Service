package com.example.surveyapp.Model.Dto;

import java.util.List;

public record QuestionReportDto(
        Long questionId,
        String questionText,
        Double averageScore,
        List<UserAnswerDto> userAnswers
) {}
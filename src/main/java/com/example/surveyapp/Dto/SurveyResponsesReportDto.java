package com.example.surveyapp.Dto;

import java.util.List;

public record SurveyResponsesReportDto(
        String surveyId,
        String surveyName,
        List<QuestionReportDto> questions
) {}
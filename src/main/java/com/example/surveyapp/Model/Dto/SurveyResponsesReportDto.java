package com.example.surveyapp.Model.Dto;

import java.util.List;

public record SurveyResponsesReportDto(
        String surveyId,
        String surveyName,
        List<QuestionReportDto> questions
) {}
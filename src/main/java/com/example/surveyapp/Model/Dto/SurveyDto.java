package com.example.surveyapp.Model.Dto;

import java.time.LocalDateTime;
import java.util.List;

public record SurveyDto(
        String surveyId,
        String name,
        String status,
        LocalDateTime startDate,
        LocalDateTime endDate,
        List<String> usersToSend,
        List<SectionDto> sections // Hiyerarşik yapı: Bölümler ve içindeki sorular
) {
}
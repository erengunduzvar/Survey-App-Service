package com.example.surveyapp.Model.Dto;

import com.example.surveyapp.Model.Entity.Survey;

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
    public static SurveyDto mapToDto(Survey entity) {
        return new SurveyDto(
                entity.getSurveyId(),
                entity.getName(),
                entity.getStatus(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getUsersToSend(),
                entity.getSections() != null ?
                        entity.getSections().stream().map(SectionDto::mapToDto).toList() :
                        List.of()
        );
    }
}
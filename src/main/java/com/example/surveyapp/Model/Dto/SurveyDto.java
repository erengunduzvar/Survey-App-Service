package com.example.surveyapp.Model.Dto;

import com.example.surveyapp.Model.Entity.Survey;
import com.example.surveyapp.Model.Enum.SurveyStatus;

import java.time.LocalDateTime;
import java.util.List;

public record SurveyDto(
        String surveyId,
        String name,
        SurveyStatus status, // String yerine Enum
        LocalDateTime startDate,
        LocalDateTime endDate,
        List<String> usersToSend,
        List<SectionDto> sections
) {
    public static SurveyDto mapToDto(Survey entity) {
        return new SurveyDto(
                entity.getSurveyId(),
                entity.getName(),
                entity.getStatus(), // Enum değeri döner
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getUsersToSend(),
                entity.getSections() != null ?
                        entity.getSections().stream().map(SectionDto::mapToDto).toList() :
                        List.of()
        );
    }
    public static SurveyDto mapToDtoWithoutDetails(Survey entity) {
        return new SurveyDto(
                entity.getSurveyId(),
                entity.getName(),
                entity.getStatus(), // Enum değeri döner
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getUsersToSend(),
                null
        );
    }
}
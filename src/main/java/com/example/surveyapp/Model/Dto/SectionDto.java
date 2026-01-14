package com.example.surveyapp.Model.Dto;

import com.example.surveyapp.Model.Entity.Section;

import java.util.List;

public record SectionDto(
        Long sectionId,
        String sectionName,
        Integer priority,
        String surveyId,             // Üst nesne (Survey) referansı
        List<QuestionDto> questions  // Alt nesne (Questions) listesi - DTO olarak
) {
    public static SectionDto mapToDto(Section entity) {
        return new SectionDto(
                entity.getSectionId(),
                entity.getSectionName(),
                entity.getPriority(),
                entity.getSurvey() != null ? entity.getSurvey().getSurveyId() : null,
                entity.getQuestions() != null ?
                        entity.getQuestions().stream().map(QuestionDto::mapToDto).toList() :
                        List.of()
        );
    }
}
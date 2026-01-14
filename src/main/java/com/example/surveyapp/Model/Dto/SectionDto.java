package com.example.surveyapp.Model.Dto;

import java.util.List;

public record SectionDto(
        Long sectionId,
        String sectionName,
        Integer priority,
        String surveyId,             // Üst nesne (Survey) referansı
        List<QuestionDto> questions  // Alt nesne (Questions) listesi - DTO olarak
) {
}
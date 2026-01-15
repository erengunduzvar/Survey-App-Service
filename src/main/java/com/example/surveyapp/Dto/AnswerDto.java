package com.example.surveyapp.Dto;

import com.example.surveyapp.Entity.Answers;

public record AnswerDto(
        Long id,
        String answer,
        Long questionId,
        String surveyId
) {
    /**
     * Answers entity nesnesini AnswerDto record yapısına dönüştürür.
     * * @param entity Veritabanından gelen cevap nesnesi
     * @return API için hazırlanan cevap DTO'su
     */
    public static AnswerDto mapToDto(Answers entity) {
        return new AnswerDto(
                entity.getId(),
                entity.getAnswer(),
                entity.getQuestion() != null ? entity.getQuestion().getQuestionId() : null,
                entity.getSurvey() != null ? entity.getSurvey().getSurveyId() : null
        );
    }
}

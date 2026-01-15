package com.example.surveyapp.Dto;

import com.example.surveyapp.Entity.Questions;
import com.example.surveyapp.Enum.QuestionTypeEnum;

public record QuestionDto(
        Long questionId,
        QuestionTypeEnum questionType,     // Veya daha önce oluşturduğumuz QuestionType enum'ı
        Integer questionPriority,
        String questionText,
        String questionAnswers,
        Long sectionId,          // Entity'deki Section objesinin ID'si
        String surveyId          // Entity'deki Survey objesinin ID'si
) {
    public static QuestionDto mapToDto(Questions entity) {
        return new QuestionDto(
                entity.getQuestionId(),
                entity.getQuestionType(),
                entity.getQuestionPriority(),
                entity.getQuestionText(),
                entity.getQuestionAnswers(),
                entity.getSection() != null ? entity.getSection().getSectionId() : null,
                entity.getSurvey() != null ? entity.getSurvey().getSurveyId() : null
        );
    }
}

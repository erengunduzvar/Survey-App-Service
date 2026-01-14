package com.example.surveyapp.Model.Dto;

public record QuestionDto(
        Long questionId,
        String questionType,     // Veya daha önce oluşturduğumuz QuestionType enum'ı
        Integer questionPriority,
        String questionText,
        String questionAnswers,
        Long sectionId,          // Entity'deki Section objesinin ID'si
        String surveyId          // Entity'deki Survey objesinin ID'si
) { }

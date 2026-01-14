package com.example.surveyapp;

import lombok.Getter;

@Getter
public enum QuestionType {

    LIKERT("Likert"),
    TEXT("Text");

    private final String value;

    // Lombok yerine constructor'ı manuel yazıyoruz
    QuestionType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
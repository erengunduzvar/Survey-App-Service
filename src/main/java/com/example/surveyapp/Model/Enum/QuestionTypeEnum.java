package com.example.surveyapp.Model.Enum;

import lombok.Getter;

@Getter
public enum QuestionTypeEnum {

    LIKERT("Likert"),
    TEXT("Text");

    private final String value;

QuestionTypeEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
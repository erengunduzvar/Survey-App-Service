package com.example.surveyapp.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SurveyStatus {
    DRAFT("DRAFT"),          // Taslak aşaması, üzerinde değişiklik yapılabilir.
    PUBLISHED("PUBLISHED");  // Yayınlanmış, artık içeriği değiştirilemez/sabit.

    private final String value;

    @Override
    public String toString() {
        return this.value;
    }
}
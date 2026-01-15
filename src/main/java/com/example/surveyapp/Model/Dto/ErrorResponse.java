package com.example.surveyapp.Model.Dto;

public record ErrorResponse(
        int status,
        String message,
        long timestamp
) {
    // İstersen timestamp'i otomatik alan bir constructor ekleyebilirsin
    public ErrorResponse(int status, String message) {
        this(status, message, System.currentTimeMillis());
    }
}

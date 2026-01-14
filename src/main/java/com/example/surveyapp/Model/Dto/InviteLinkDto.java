package com.example.surveyapp.Model.Dto;

import java.time.LocalDateTime;

public record InviteLinkDto(
        Long inviteId,
        String createdBy,
        String invitedUserId,
        String surveyId,         // Entity'deki Survey objesinin ID'si
        Boolean isSurveyComplete,
        String inviteToken,
        LocalDateTime tokenExpireDate
) {

    public boolean isExpired() {
        return tokenExpireDate != null && tokenExpireDate.isBefore(LocalDateTime.now());
    }
}

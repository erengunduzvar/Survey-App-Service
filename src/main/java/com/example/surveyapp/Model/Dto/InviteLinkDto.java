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

    public static InviteLinkDto mapToDto(com.example.surveyapp.Model.Entity.InviteLink entity) {
        return new InviteLinkDto(
                entity.getInviteId(),
                entity.getCreatedBy(),
                entity.getInvitedUserId(),
                entity.getSurvey() != null ? entity.getSurvey().getSurveyId() : null,
                entity.getIsSurveyComplete(),
                entity.getInviteToken(),
                entity.getTokenExpireDate()
        );
    }

    public boolean isExpired() {
        return tokenExpireDate != null && tokenExpireDate.isBefore(LocalDateTime.now());
    }
}

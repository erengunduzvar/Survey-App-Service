package com.example.surveyapp.Dto;

import com.example.surveyapp.Entity.InviteLink;

import java.time.LocalDateTime;

public record InviteLinkDto(
        Long inviteId,
        String createdBy,
        String invitedUserMail,
        String surveyId,         // Entity'deki Survey objesinin ID'si
        Boolean isSurveyComplete,
        String inviteToken
) {

    public static InviteLinkDto mapToDto(InviteLink entity) {
        return new InviteLinkDto(
                entity.getInviteId(),
                entity.getCreatedBy(),
                entity.getInvitedUserMail(),
                entity.getSurvey() != null ? entity.getSurvey().getSurveyId() : null,
                entity.getIsSurveyComplete(),
                entity.getInviteToken()
        );
    }
}

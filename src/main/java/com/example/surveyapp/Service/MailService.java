package com.example.surveyapp.Service;

import com.example.surveyapp.Entity.InviteLink;
import com.example.surveyapp.Entity.Survey;

public interface MailService {
    void sendSimpleMail(String to, String subject, Survey survey, InviteLink inviteLink);
}

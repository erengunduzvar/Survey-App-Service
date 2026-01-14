package com.example.surveyapp.Service;

import com.example.surveyapp.Model.Entity.InviteLink;
import com.example.surveyapp.Model.Entity.Survey;

public interface IMailService {
    void sendSimpleMail(String to, String subject, String text, Survey survey, InviteLink inviteLink);
}

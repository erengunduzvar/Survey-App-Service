package com.example.surveyapp.Service.Impl;

import com.example.surveyapp.Service.IInviteLinkService;
import com.example.surveyapp.Service.IInviteTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class InviteLinkServiceImpl implements IInviteLinkService {

    @Value("${survey.invite.base-url}")
    private String baseUrl;

    private final IInviteTokenService inviteTokenService;

    public InviteLinkServiceImpl(IInviteTokenService inviteTokenService) {
        this.inviteTokenService = inviteTokenService;
    }

    public String generateInviteLink(String email, String surveyId) {
        String token = inviteTokenService.generateToken(email, surveyId);

        return baseUrl + "/" + surveyId + "?token=" +
                URLEncoder.encode(token, StandardCharsets.UTF_8);
    }
}


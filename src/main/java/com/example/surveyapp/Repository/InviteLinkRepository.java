package com.example.surveyapp.Repository;

import com.example.surveyapp.Entity.InviteLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InviteLinkRepository extends JpaRepository<InviteLink, Long> {
    // Kullanıcı linke tıkladığında token doğrulaması için
    Optional<InviteLink> findByInviteToken(String inviteToken);
    void deleteBySurvey_SurveyIdAndInvitedUserMailIn(String surveyId, List<String> userEmails);
}
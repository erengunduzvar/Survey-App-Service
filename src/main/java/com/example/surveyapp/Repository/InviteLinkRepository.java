package com.example.surveyapp.Repository;

import com.example.surveyapp.Model.Entity.InviteLink;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InviteLinkRepository extends JpaRepository<InviteLink, Long> {
    // Kullanıcı linke tıkladığında token doğrulaması için
    Optional<InviteLink> findByInviteToken(String inviteToken);
}
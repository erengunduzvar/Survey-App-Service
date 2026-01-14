package com.example.surveyapp.Model.Entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDateTime;

@Entity
@Table(name = "invite_links")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InviteLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inviteId;

    private String createdBy; // Olusturan kullanıcı ID veya email
    private String invitedUserId; // Davet edilen kullanıcı ID veya email

    // Bir anketin birçok davet linki olabilir (ManyToOne)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    @JsonBackReference(value = "survey-invite") // Sonsuz döngü engelleme
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Survey survey;

    private Boolean isSurveyComplete;

    @Column(unique = true)
    private String inviteToken;

    private LocalDateTime tokenExpireDate;
}
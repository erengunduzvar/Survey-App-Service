package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//@Entity
//@Table(name = "invite_links")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InviteLink {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inviteId;

    private String createdBy;
    private String invitedUserId;
    private String surveyId;
    private Boolean isSurveyComplete;
    private String inviteToken;
    private LocalDateTime tokenExpireDate;
}

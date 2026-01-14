package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

//
//@Entity
//@Table(name = "surveys")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Survey {
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
    private String surveyId;

    private String name;
    private String status;

    @Builder.Default
    private LocalDateTime startDate = LocalDateTime.now();

    private LocalDateTime endDate;

//    @ElementCollection
    private List<String> usersToSend;
}

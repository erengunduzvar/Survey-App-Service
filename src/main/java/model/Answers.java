package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity
//@Table(name = "answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answers {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Genel bir ID eklemek iyi uygulamadır

    private Long questionId;
    private String answer;
    private String surveyId;
}

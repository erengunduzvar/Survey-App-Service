package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity
//@Table(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Questions {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    private String questionType;
    private Integer questionPriority;
    private String questionText;
    private String questionAnswers; // JSON veya virgülle ayrılmış string olarak tutulabilir

    private String surveyId;
    private Long sectionId;
}

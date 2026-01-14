package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity
//@Table(name = "sections")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sectionId;

    private String sectionName;
    private String anketId; // Survey tablosuyla ilişki
    private Integer priority;
}

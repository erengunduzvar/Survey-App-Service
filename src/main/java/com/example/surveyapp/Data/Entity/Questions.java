package com.example.surveyapp.Data.Entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Questions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    private String questionType;
    private Integer questionPriority;
    private String questionText;

    @Column(columnDefinition = "TEXT") // Uzun cevap seçenekleri için
    private String questionAnswers;

    // Soru hangi bölüme ait?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    @JsonBackReference(value = "section-question")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Section section;

    // Soru hangi ankete ait? (Hızlı erişim için direkt ilişki)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    @JsonBackReference(value = "survey-question")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Survey survey;
}
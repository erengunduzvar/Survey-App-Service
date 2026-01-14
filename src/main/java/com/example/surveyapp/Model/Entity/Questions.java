package com.example.surveyapp.Model.Entity;

import com.example.surveyapp.Model.Enum.QuestionTypeEnum;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private QuestionTypeEnum questionType = QuestionTypeEnum.TEXT;
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

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "question-answer") // Sonsuz döngü engelleme
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Answers> answers = new ArrayList<>();
}
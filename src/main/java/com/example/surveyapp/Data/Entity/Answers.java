package com.example.surveyapp.Data.Entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String answer;

    // Bu cevap hangi soruya verildi?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    @JsonBackReference(value = "question-answer")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Questions question;

    // Bu cevap hangi anket kapsamında verildi?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    @JsonBackReference(value = "survey-answer")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Survey survey;
}
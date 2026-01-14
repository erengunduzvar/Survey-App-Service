package com.example.surveyapp.Model.Entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sections")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sectionId;

    private String sectionName;
    private Integer priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    @JsonBackReference(value = "survey-section") // Survey ile ilişki ismi
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Survey survey;

    // Bölüme bağlı sorular
    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "section-question") // Soru listesini JSON'da gösterir
    @Builder.Default
    private List<Questions> questions = new ArrayList<>();
}
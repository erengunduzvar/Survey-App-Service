package com.example.surveyapp.Repository;

import com.example.surveyapp.Model.Entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface QuestionsRepository extends JpaRepository<Questions, Long> {
    List<Questions> findBySection_SectionIdOrderByQuestionPriorityAsc(Long sectionId);
    List<Questions> findBySurvey_SurveyId(String surveyId);
    Optional<Questions> findByQuestionId(Long questionId);
}
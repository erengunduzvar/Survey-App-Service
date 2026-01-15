package com.example.surveyapp.Repository;

import com.example.surveyapp.Entity.Answers;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnswersRepository extends JpaRepository<Answers, Long> {
    List<Answers> findBySurvey_SurveyId(String surveyId);
    List<Answers> findByQuestion_QuestionId(Long questionId);
}

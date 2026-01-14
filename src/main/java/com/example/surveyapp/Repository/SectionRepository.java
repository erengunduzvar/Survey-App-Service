package com.example.surveyapp.Repository;

import com.example.surveyapp.Data.Entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findBySurvey_SurveyIdOrderByPriorityAsc(String surveyId);
}

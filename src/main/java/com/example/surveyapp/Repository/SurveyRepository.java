package com.example.surveyapp.Repository;

import com.example.surveyapp.Model.Entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, String> {
}
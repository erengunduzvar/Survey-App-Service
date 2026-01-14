package com.example.surveyapp.Service;

import com.example.surveyapp.Model.Dto.SurveyDto;
import com.example.surveyapp.Model.Entity.Questions;
import com.example.surveyapp.Model.Entity.Section;
import com.example.surveyapp.Model.Entity.Survey;
import com.example.surveyapp.Repository.QuestionsRepository;
import com.example.surveyapp.Repository.SectionRepository;
import com.example.surveyapp.Repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final SectionRepository sectionRepository;
    private final QuestionsRepository questionsRepository;


    
}

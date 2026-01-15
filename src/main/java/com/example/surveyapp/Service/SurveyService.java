package com.example.surveyapp.Service;

import com.example.surveyapp.Dto.SurveyDto;
import com.example.surveyapp.Dto.SurveyResponsesReportDto;

import java.util.List;

public interface SurveyService {
    List<SurveyDto> findAll();
    SurveyDto save(SurveyDto dto);
    SurveyDto findById(String surveyId);
    SurveyDto update(String surveyId, SurveyDto dto);
    void delete(String surveyId);
    void duplicateSurvey(String surveyId, String newName);
    SurveyResponsesReportDto getResponsesReport(String surveyId);
}

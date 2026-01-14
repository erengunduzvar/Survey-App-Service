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

    // 1. Hiyerarşik Anket Detayı Getirme
    @Transactional(readOnly = true)
    public SurveyDto getSurveyDetail(String surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Anket bulunamadı"));
        return SurveyDto.mapToDto(survey); // Tüm alt bölümler ve sorularla birlikte
    }

    // 2. Anket Çoklama (Duplicate) - OpenAPI spesifikasyonundaki kritik işlem
    @Transactional
    public void duplicateSurvey(String surveyId, String newName) {
        Survey original = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Kaynak anket bulunamadı"));

        // Yeni Anket Oluşturma
        Survey copy = Survey.builder()
                .name(newName)
                .status("DRAFT")
                .usersToSend(original.getUsersToSend())
                .build();
        Survey savedCopy = surveyRepository.save(copy);

        // Derin Kopyalama (Deep Copy): Sections -> Questions
        original.getSections().forEach(origSec -> {
            Section secCopy = Section.builder()
                    .sectionName(origSec.getSectionName())
                    .priority(origSec.getPriority())
                    .survey(savedCopy)
                    .build();
            Section savedSec = sectionRepository.save(secCopy);

            origSec.getQuestions().forEach(origQ -> {
                Questions qCopy = Questions.builder()
                        .questionText(origQ.getQuestionText())
                        .questionType(origQ.getQuestionType())
                        .questionPriority(origQ.getQuestionPriority())
                        .questionAnswers(origQ.getQuestionAnswers())
                        .survey(savedCopy)
                        .section(savedSec)
                        .build();
                questionsRepository.save(qCopy);
            });
        });
    }
}

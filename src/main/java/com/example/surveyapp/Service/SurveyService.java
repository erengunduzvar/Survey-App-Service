package com.example.surveyapp.Service;

import com.example.surveyapp.Model.Dto.QuestionReportDto;
import com.example.surveyapp.Model.Dto.SurveyDto;
import com.example.surveyapp.Model.Dto.SurveyResponsesReportDto;
import com.example.surveyapp.Model.Dto.UserAnswerDto;
import com.example.surveyapp.Model.Entity.Questions;
import com.example.surveyapp.Model.Entity.Section;
import com.example.surveyapp.Model.Entity.Survey;
import com.example.surveyapp.Model.Enum.SurveyStatus;
import com.example.surveyapp.Repository.QuestionsRepository;
import com.example.surveyapp.Repository.SectionRepository;
import com.example.surveyapp.Repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final SectionRepository sectionRepository;
    private final QuestionsRepository questionsRepository;

    // 1. GET /surveys - Tüm anketleri listele
    @Transactional(readOnly = true)
    public List<SurveyDto> findAll() {
        return surveyRepository.findAll().stream()
                .map(SurveyDto::mapToDto)
                .toList();
    }

    // 2. POST /surveys - Yeni anket oluştur (Hiyerarşik)
    @Transactional
    public SurveyDto save(SurveyDto dto) {
        Survey survey = Survey.builder()
                .name(dto.name())
                .status(SurveyStatus.DRAFT)
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .usersToSend(dto.usersToSend())
                .build();

        return SurveyDto.mapToDto(surveyRepository.save(survey));
    }

    // 3. GET /surveys/{surveyId} - Hiyerarşik anket detayı
    @Transactional(readOnly = true)
    public SurveyDto findById(String surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Anket bulunamadı: " + surveyId));
        return SurveyDto.mapToDto(survey);
    }

    // 4. PUT /surveys/{surveyId} - Anket güncelle
    @Transactional
    public SurveyDto update(String surveyId, SurveyDto dto) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Anket bulunamadı"));

        survey.setName(dto.name());
        survey.setStatus(dto.status());
        survey.setEndDate(dto.endDate());
        survey.setUsersToSend(dto.usersToSend());

        return SurveyDto.mapToDto(surveyRepository.save(survey));
    }

    // 5. DELETE /surveys/{surveyId} - Anket sil
    @Transactional
    public void delete(String surveyId) {
        if (!surveyRepository.existsById(surveyId)) {
            throw new RuntimeException("Silinecek anket bulunamadı");
        }
        surveyRepository.deleteById(surveyId);
    }

    // 6. POST /surveys/{surveyId}/duplicate - Anket Çoklama (Deep Copy)
    @Transactional
    public void duplicateSurvey(String surveyId, String newName) {
        Survey original = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Kaynak anket bulunamadı"));

        Survey copy = Survey.builder()
                .name(newName)
                .status(SurveyStatus.DRAFT)
                .usersToSend(original.getUsersToSend())
                .build();
        Survey savedCopy = surveyRepository.save(copy);

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

    // 7. GET /surveys/{surveyId}/responses - Soru bazlı cevap raporu
    @Transactional(readOnly = true)
    public SurveyResponsesReportDto getResponsesReport(String surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Anket bulunamadı"));

        List<QuestionReportDto> questionReports = survey.getSections().stream()
                .flatMap(section -> section.getQuestions().stream())
                .map(q -> new QuestionReportDto(
                        q.getQuestionId(),
                        q.getQuestionText(),
                        q.getAnswers().stream()
                                .map(a -> new UserAnswerDto(1L, a.getAnswer())) // Örnek: userId 1L
                                .toList()
                )).toList();

        return new SurveyResponsesReportDto(survey.getSurveyId(), survey.getName(), questionReports);
    }



}

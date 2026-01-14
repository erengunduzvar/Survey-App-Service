package com.example.surveyapp.Service.Impl;

import com.example.surveyapp.Model.Dto.QuestionReportDto;
import com.example.surveyapp.Model.Dto.SurveyDto;
import com.example.surveyapp.Model.Dto.SurveyResponsesReportDto;
import com.example.surveyapp.Model.Dto.UserAnswerDto;
import com.example.surveyapp.Model.Entity.Answers;
import com.example.surveyapp.Model.Entity.Questions;
import com.example.surveyapp.Model.Entity.Section;
import com.example.surveyapp.Model.Entity.Survey;
import com.example.surveyapp.Model.Enum.QuestionTypeEnum;
import com.example.surveyapp.Model.Enum.SurveyStatus;
import com.example.surveyapp.Repository.QuestionsRepository;
import com.example.surveyapp.Repository.SectionRepository;
import com.example.surveyapp.Repository.SurveyRepository;
import com.example.surveyapp.Service.ISurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SurveyServiceImpl implements ISurveyService {
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
    @Override
    @Transactional
    public SurveyDto update(String surveyId, SurveyDto dto) {
        // 1. Mevcut anketi veritabanından getir
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Anket bulunamadı"));

        // 2. KRİTİK KONTROL: Sadece DRAFT olanlar değişebilir
        if (survey.getStatus() != SurveyStatus.DRAFT) {
            throw new RuntimeException("Hata: Sadece 'TASLAK' (DRAFT) durumundaki anketler üzerinde değişiklik yapılabilir.");
        }

        // 3. Eğer anket DRAFT ise güncellemeleri uygula
        survey.setName(dto.name());
        survey.setEndDate(dto.endDate());
        survey.setUsersToSend(dto.usersToSend());
        // Not: startDate genellikle oluşunca sabit kalır, bu yüzden update'e eklemedik.

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
                        calculateAverageScoreInternal(q), // Puan hesaplama burada çağrılıyor
                        q.getAnswers().stream()
                                .map(a -> new UserAnswerDto(1L, a.getAnswer()))
                                .toList()
                )).toList();

        return new SurveyResponsesReportDto(survey.getSurveyId(), survey.getName(), questionReports);
    }
    // Helper metod: Dışarıdan id almak yerine direkt nesne ile çalışır
    private Double calculateAverageScoreInternal(Questions question) {
        // 1. Sadece LIKERT tipindeyse hesaplama yap
        if (question.getQuestionType() != QuestionTypeEnum.LIKERT || question.getQuestionAnswers() == null) {
            return null; // Likert değilse score null döner
        }

        // 2. Seçenekleri parse et (kötü, orta, iyi -> 1, 2, 3)
        String[] options = question.getQuestionAnswers().split(",");
        Map<String, Integer> scoreMap = new HashMap<>();
        for (int i = 0; i < options.length; i++) {
            scoreMap.put(options[i].trim(), i + 1);
        }

        List<Answers> userAnswers = question.getAnswers();
        if (userAnswers == null || userAnswers.isEmpty()) return 0.0;

        // 3. Puanları topla
        double totalScore = 0;
        int validAnswerCount = 0;

        for (Answers answer : userAnswers) {
            if (answer.getAnswer() != null) {
                Integer score = scoreMap.get(answer.getAnswer().trim());
                if (score != null) {
                    totalScore += score;
                    validAnswerCount++;
                }
            }
        }

        // 4. Ortalamayı döndür
        return validAnswerCount > 0 ? (totalScore / validAnswerCount) : 0.0;
    }



}

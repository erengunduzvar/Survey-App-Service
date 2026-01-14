package com.example.surveyapp.Service.Impl;

import com.example.surveyapp.Model.Dto.*;
import com.example.surveyapp.Model.Entity.*;
import com.example.surveyapp.Model.Enum.QuestionTypeEnum;
import com.example.surveyapp.Model.Enum.SurveyStatus;
import com.example.surveyapp.Repository.InviteLinkRepository;
import com.example.surveyapp.Repository.QuestionsRepository;
import com.example.surveyapp.Repository.SectionRepository;
import com.example.surveyapp.Repository.SurveyRepository;
import com.example.surveyapp.Service.IInviteTokenService;
import com.example.surveyapp.Service.IMailService;
import com.example.surveyapp.Service.ISurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyServiceImpl implements ISurveyService {
    private final SurveyRepository surveyRepository;
    private final SectionRepository sectionRepository;
    private final QuestionsRepository questionsRepository;
    private final InviteLinkRepository inviteLinkRepository;
    private final IInviteTokenService inviteTokenService;
    private final IMailService mailService;

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
        // 1. Survey nesnesini oluştur
        Survey survey = Survey.builder()
                .name(dto.name())
                .status(SurveyStatus.DRAFT)
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .usersToSend(dto.usersToSend())
                .build();

        // 2. Eğer DTO içinde sectionlar varsa, onları da bağla
        if (dto.sections() != null) {
            List<Section> sections = dto.sections().stream().map(sDto -> {
                Section section = Section.builder()
                        .sectionName(sDto.sectionName())
                        .priority(sDto.priority())
                        .survey(survey) // Survey bağlantısı
                        .build();

                // 3. Section içindeki soruları bağla
                if (sDto.questions() != null) {
                    List<Questions> questions = sDto.questions().stream().map(qDto ->
                            Questions.builder()
                                    .questionText(qDto.questionText())
                                    .questionType(qDto.questionType())
                                    .questionPriority(qDto.questionPriority())
                                    .questionAnswers(qDto.questionAnswers())
                                    .section(section) // Section bağlantısı
                                    .survey(survey)   // Survey bağlantısı
                                    .build()
                    ).collect(Collectors.toList());
                    section.setQuestions(questions);
                }
                return section;
            }).collect(Collectors.toList());
            survey.setSections(sections);
        }

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
        // 1. Mevcut anketi getir
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Anket bulunamadı"));

        // 2. Durum kontrolü
        if (survey.getStatus() != SurveyStatus.DRAFT) {
            throw new RuntimeException("Sadece TASLAK anketler güncellenebilir.");
        }

        // 3. Temel alanları güncelle
        survey.setName(dto.name());
        survey.setEndDate(dto.endDate());
        survey.setUsersToSend(dto.usersToSend());

        // 4. SECTIONS VE QUESTIONS GÜNCELLEME (Senkronizasyon)
        if (dto.sections() != null) {
            // Mevcut bölümleri temizleyip DTO'dakileri eklemek en temiz yoldur (ID'ler korunacaksa eşleme yapılır)
            // Ancak en garantisi mevcut listeyi yönetmektir:
            updateSections(survey, dto);
        }

        if(dto.status() == SurveyStatus.PUBLISHED && dto.sections() == null || dto.sections().getFirst().questions() == null) {
            throw new RuntimeException("Boş anket gönderilemez");
        }

        if(dto.status() == SurveyStatus.PUBLISHED) {
            List<String> usersToSend = survey.getUsersToSend();
            for (String mail : usersToSend) {
                String inviteToken =  inviteTokenService.generateToken(mail,surveyId);
                InviteLink inviteLink = new InviteLink();
                inviteLink.setInviteToken(inviteToken);
                inviteLink.setSurvey(survey);
                inviteLink.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
                inviteLinkRepository.save(inviteLink);
                //mailService.sendSimpleMail(mail,survey.getName(),survey,inviteLink);
            }

        }

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

    private void updateSections(Survey survey, SurveyDto dto) {
        // Mevcut bölümleri bir haritaya al (Hızlı erişim için)
        Map<Long, Section> existingSections = new HashMap<>();
        if (survey.getSections() != null) {
            survey.getSections().forEach(s -> existingSections.put(s.getSectionId(), s));
        }

        // Yeni bölüm listesini oluştur
        List<Section> updatedSections = dto.sections().stream().map(sDto -> {
            Section section;
            if (sDto.sectionId() != null && existingSections.containsKey(sDto.sectionId())) {
                // MEVCUT BÖLÜMÜ GÜNCELLE
                section = existingSections.get(sDto.sectionId());
                section.setSectionName(sDto.sectionName());
                section.setPriority(sDto.priority());
            } else {
                // YENİ BÖLÜM EKLE
                section = Section.builder()
                        .sectionName(sDto.sectionName())
                        .priority(sDto.priority())
                        .survey(survey)
                        .build();
            }

            // BÖLÜMÜN SORULARINI GÜNCELLE
            updateQuestions(section, sDto, survey);

            return section;
        }).collect(Collectors.toList());

        // Survey'in section listesini güncelle (orphanRemoval sayesinde eskiler silinir)
        survey.getSections().clear();
        survey.getSections().addAll(updatedSections);
    }

    private void updateQuestions(Section section, SectionDto sDto, Survey survey) {
        Map<Long, Questions> existingQuestions = new HashMap<>();
        if (section.getQuestions() != null) {
            section.getQuestions().forEach(q -> existingQuestions.put(q.getQuestionId(), q));
        }

        List<Questions> updatedQuestions = sDto.questions().stream().map(qDto -> {
            if (qDto.questionId() != null && existingQuestions.containsKey(qDto.questionId())) {
                // MEVCUT SORUYU GÜNCELLE
                Questions q = existingQuestions.get(qDto.questionId());
                q.setQuestionText(qDto.questionText());
                q.setQuestionType(qDto.questionType());
                q.setQuestionPriority(qDto.questionPriority());
                q.setQuestionAnswers(qDto.questionAnswers());
                return q;
            } else {
                // YENİ SORU EKLE
                return Questions.builder()
                        .questionText(qDto.questionText())
                        .questionType(qDto.questionType())
                        .questionPriority(qDto.questionPriority())
                        .questionAnswers(qDto.questionAnswers())
                        .section(section)
                        .survey(survey)
                        .build();
            }
        }).collect(Collectors.toList());

        section.getQuestions().clear();
        section.getQuestions().addAll(updatedQuestions);
    }



}

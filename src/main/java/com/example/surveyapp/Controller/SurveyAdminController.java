package com.example.surveyapp.Controller;

import com.example.surveyapp.Model.Dto.SurveyDto;
import com.example.surveyapp.Model.Dto.SurveyResponsesReportDto;
import com.example.surveyapp.Service.ISurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/surveys")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class SurveyAdminController {
    private final ISurveyService surveyService;

    // 1. Tüm anketleri listele
    @GetMapping
    public ResponseEntity<List<SurveyDto>> getAllSurveys() {
        return ResponseEntity.ok(surveyService.findAll());
    }

    // 2. Yeni anket oluştur (Hiyerarşik: Section ve Question'lar dahil)
    @PostMapping
    public ResponseEntity<SurveyDto> createSurvey(@RequestBody SurveyDto surveyDto) {
        return new ResponseEntity<>(surveyService.save(surveyDto), HttpStatus.CREATED);
    }

    // 3. Tek bir anketin detayını getir (ID ile)
    @GetMapping("/{surveyId}")
    public ResponseEntity<SurveyDto> getSurveyById(@PathVariable String surveyId) {
        return ResponseEntity.ok(surveyService.findById(surveyId));
    }

    // 4. Mevcut anketi güncelle (Sadece DRAFT durumundakiler)
    @PutMapping("/{surveyId}")
    public ResponseEntity<SurveyDto> updateSurvey(
            @PathVariable String surveyId,
            @RequestBody SurveyDto surveyDto) {
        return ResponseEntity.ok(surveyService.update(surveyId, surveyDto));
    }

    // 5. Anketi sil
    @DeleteMapping("/{surveyId}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable String surveyId) {
        surveyService.delete(surveyId);
        return ResponseEntity.noContent().build();
    }

    // 6. Anketi çokla (Deep Copy)
    // Query param olarak yeni isim alır: /surveys/{id}/duplicate?newName=YeniAnketIsmi
    @PostMapping("/{surveyId}/duplicate")
    public ResponseEntity<Void> duplicateSurvey(
            @PathVariable String surveyId,
            @RequestParam String newName) {
        surveyService.duplicateSurvey(surveyId, newName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 7. Anket bazlı cevap raporu ve skor ortalamaları
    @GetMapping("/{surveyId}/responses")
    public ResponseEntity<SurveyResponsesReportDto> getResponsesReport(@PathVariable String surveyId) {
        return ResponseEntity.ok(surveyService.getResponsesReport(surveyId));
    }
}

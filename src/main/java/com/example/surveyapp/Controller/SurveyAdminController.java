package com.example.surveyapp.Controller;

import com.example.surveyapp.Dto.SurveyDto;
import com.example.surveyapp.Dto.SurveyResponsesReportDto;
import com.example.surveyapp.Enum.SurveyStatus;
import com.example.surveyapp.Service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(
        origins = "http://localhost:5173",
        allowedHeaders = {"Authorization", "Content-Type"},
        methods = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.OPTIONS}
)
@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class SurveyAdminController {
    private final SurveyService surveyService;

    @GetMapping
    public ResponseEntity<List<SurveyDto>> getAllSurveys() {
        return ResponseEntity.ok(surveyService.findAll());
    }

    @PostMapping
    public ResponseEntity<SurveyDto> createSurvey(@RequestBody SurveyDto surveyDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(surveyService.save(surveyDto));
    }

    @GetMapping("/{surveyId}")
    public ResponseEntity<SurveyDto> getSurveyById(@PathVariable String surveyId) {
        return ResponseEntity.ok(surveyService.findById(surveyId));
    }

    @PutMapping("/{surveyId}")
    public ResponseEntity<SurveyDto> updateSurvey(@PathVariable String surveyId, @RequestBody SurveyDto surveyDto) {
        return ResponseEntity.ok(surveyService.update(surveyId, surveyDto));
    }

    @DeleteMapping("/{surveyId}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable String surveyId) {
        surveyService.delete(surveyId); // Mantık servise taşındı
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{surveyId}/duplicate")
    public ResponseEntity<Void> duplicateSurvey(@PathVariable String surveyId, @RequestBody String newName) {
        surveyService.duplicateSurvey(surveyId, newName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{surveyId}/responses")
    public ResponseEntity<SurveyResponsesReportDto> getResponsesReport(@PathVariable String surveyId) {
        return ResponseEntity.ok(surveyService.getResponsesReport(surveyId));
    }
    // 8. Ankete toplu katılımcı ekle ve davet linki oluştur
    @PostMapping("/{surveyId}/add-people")
    public ResponseEntity<Void> addPeopleToSurvey(
            @PathVariable String surveyId,
            @RequestBody List<String> userEmails) {

        surveyService.addPeopleToSurvey(surveyId, userEmails);
        return ResponseEntity.ok().build();
    }

    // 9. Ankete ait davet linklerini (katılımcıları) toplu sil
    @DeleteMapping("/{surveyId}/remove-people")
    public ResponseEntity<Void> deletePeopleToSurvey(
            @PathVariable String surveyId,
            @RequestBody List<String> userEmails) {

        surveyService.deletePeopleToSurvey(surveyId, userEmails);
        return ResponseEntity.noContent().build(); // 204 No Content dönmek silme işlemleri için standarttır
    }


}

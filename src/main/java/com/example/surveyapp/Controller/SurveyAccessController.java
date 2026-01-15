package com.example.surveyapp.Controller;

import com.example.surveyapp.Dto.SurveyDto;
import com.example.surveyapp.Service.SurveyService;
import com.example.surveyapp.Validators.InviteTokenValidator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/surveyAccess")
@RequiredArgsConstructor
public class SurveyAccessController {

    private final InviteTokenValidator inviteTokenValidator;
    private final SurveyService surveyService;
    /**
     * Davet tokenı ile ankete erişim sağlar.
     * URL örneği: GET /surveyAccess/abc123token
     *
     * @param token URL'den gelen eşsiz davet anahtarı
     * @return Ankete ait bilgiler (Sorular, başlık vb.)
     */
    @GetMapping("/{token}")
    public ResponseEntity<SurveyDto> getSurveyByToken(@PathVariable String token) {
        // Servis katmanında token kontrolü (geçerlilik süresi, kullanılmış mı vb.) yapılır
        Claims claims = inviteTokenValidator.validate(token); //TODO EKLENECEK
        if(claims.getExpiration().before(new java.util.Date())){
            return ResponseEntity.status(401).build();
        }
        else {
            return ResponseEntity.ok(surveyService.findById(claims.get("surveyId", String.class)));
        }

    }
    @PostMapping("/{token}")
    public ResponseEntity<Void> postSurveyByToken(
            @PathVariable String token,
            @RequestBody SurveyDto surveyDto) {
        // Servis katmanında token kontrolü (geçerlilik süresi, kullanılmış mı vb.) yapılır
        Claims claims = inviteTokenValidator.validate(token); //TODO EKLENECEK
        if(claims.getExpiration().before(new java.util.Date())){
            return ResponseEntity.status(401).build();
        }
        else {
            surveyService.save(surveyDto);
            return ResponseEntity.ok().build();
        }
    }


}

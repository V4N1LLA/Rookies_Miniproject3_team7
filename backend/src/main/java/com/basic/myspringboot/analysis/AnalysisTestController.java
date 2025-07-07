package com.basic.myspringboot.analysis;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class AnalysisTestController {

    private final EmotionClient emotionClient;

    @PostMapping("/test")
    public ResponseEntity<?> testAnalysis(@RequestBody EmotionRequest request) {
        // **방어 로직 추가**
        if (request == null || request.getContent() == null || request.getContent().isBlank()) {
            return ResponseEntity.badRequest().body("내용이 없습니다.");
        }

        EmotionAnalysisResponse response = emotionClient.requestAnalysis(request.getContent());
        return ResponseEntity.ok(response);
    }
}
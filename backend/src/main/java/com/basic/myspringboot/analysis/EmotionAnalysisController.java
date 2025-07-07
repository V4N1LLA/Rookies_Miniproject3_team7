package com.basic.myspringboot.analysis;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analysis")
public class EmotionAnalysisController {

    private final EmotionAnalysisService analysisService;

    @PostMapping
    public Map<String, Object> analyze(@RequestBody EmotionAnalysisRequest request) {
        EmotionAnalysisResponse result = analysisService.analyze(request);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "감정 분석이 완료되었습니다.");
        response.put("data", result);
        response.put("timestamp", ZonedDateTime.now().toString());

        return response;
    }
}

package com.basic.myspringboot.analysis;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class AnalysisTestController {

    private final EmotionAnalysisService analysisService;

    @PostMapping("/test")
    public EmotionAnalysisResult testAnalysis(@RequestBody EmotionAnalysisRequest request) {
        return analysisService.analyzeAndSave(request.getContent());
    }
}

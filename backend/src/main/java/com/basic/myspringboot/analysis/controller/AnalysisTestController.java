package com.basic.myspringboot.analysis.controller;

import com.basic.myspringboot.analysis.dto.EmotionAnalysisRequest;
import com.basic.myspringboot.analysis.entity.EmotionAnalysisResult;
import com.basic.myspringboot.analysis.service.EmotionAnalysisService;
import com.basic.myspringboot.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class AnalysisTestController {

    private final EmotionAnalysisService analysisService;

    @PostMapping("/test")
    public ApiResponse<EmotionAnalysisResult> testAnalysis(@RequestBody EmotionAnalysisRequest request) {
        EmotionAnalysisResult result = analysisService.analyzeAndSave(request.getContent());

        return ApiResponse.<EmotionAnalysisResult>builder()
                .success(true)
                .message("테스트 감정 분석 완료")
                .data(result)
                .timestamp(ZonedDateTime.now().toString())
                .build();
    }
}

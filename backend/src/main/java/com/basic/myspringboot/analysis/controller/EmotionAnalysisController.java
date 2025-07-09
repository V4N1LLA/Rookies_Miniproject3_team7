package com.basic.myspringboot.analysis.controller;

import com.basic.myspringboot.analysis.dto.EmotionAnalysisRequest;
import com.basic.myspringboot.analysis.entity.EmotionAnalysisResult;
import com.basic.myspringboot.analysis.service.EmotionAnalysisService;
import com.basic.myspringboot.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analysis")
public class EmotionAnalysisController {

    private final EmotionAnalysisService analysisService;

    @PostMapping
    public ApiResponse<EmotionAnalysisResult> analyze(@RequestBody EmotionAnalysisRequest request) {
        EmotionAnalysisResult result = analysisService.analyzeAndSave(request.getContent());

        return ApiResponse.<EmotionAnalysisResult>builder()
                .success(true)
                .message("감정 분석이 완료되었습니다.")
                .data(result)
                .timestamp(ZonedDateTime.now().toString())
                .build();
    }
}

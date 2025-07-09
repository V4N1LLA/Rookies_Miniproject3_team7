package com.basic.myspringboot.analysis.controller;

import com.basic.myspringboot.analysis.dto.EmotionAnalysisRequest;
import com.basic.myspringboot.analysis.dto.EmotionAnalysisResultDto;
import com.basic.myspringboot.analysis.service.EmotionAnalysisService;
import com.basic.myspringboot.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analysis")
@Tag(name = "감정 분석", description = "감정 분석 관련 API")
public class EmotionAnalysisController {

    private final EmotionAnalysisService analysisService;

    @PostMapping
    @Operation(summary = "감정 분석 요청", description = "일기 내용을 입력받아 감정 분석을 수행하고 DB에 저장합니다.")
    public ApiResponse<EmotionAnalysisResultDto> analyze(@RequestBody EmotionAnalysisRequest request) {
        EmotionAnalysisResultDto resultDto = analysisService.analyzeAndReturnDto(request.getContent());

        return ApiResponse.<EmotionAnalysisResultDto>builder()
                .success(true)
                .message("감정 분석이 완료되었습니다.")
                .data(resultDto)
                .timestamp(ZonedDateTime.now().toString())
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "감정 분석 결과 단건 조회", description = "감정 분석 결과를 ID로 조회합니다.")
    public ApiResponse<EmotionAnalysisResultDto> getOne(@PathVariable Long id) {
        EmotionAnalysisResultDto resultDto = analysisService.getAnalysisResultDto(id);

        return ApiResponse.<EmotionAnalysisResultDto>builder()
                .success(true)
                .message("단일 감정 분석 결과 조회 성공")
                .data(resultDto)
                .timestamp(ZonedDateTime.now().toString())
                .build();
    }

    @GetMapping
    @Operation(summary = "감정 분석 전체 결과 조회", description = "모든 감정 분석 결과를 리스트로 조회합니다.")
    public ApiResponse<List<EmotionAnalysisResultDto>> getAll() {
        List<EmotionAnalysisResultDto> resultList = analysisService.getAllAnalysisResultDtos();

        return ApiResponse.<List<EmotionAnalysisResultDto>>builder()
                .success(true)
                .message("전체 감정 분석 결과 조회 성공")
                .data(resultList)
                .timestamp(ZonedDateTime.now().toString())
                .build();
    }
}

package com.basic.myspringboot.analysis.controller;

import com.basic.myspringboot.analysis.dto.EmotionAnalysisResultDto;
import com.basic.myspringboot.analysis.entity.EmotionAnalysisResult;
import com.basic.myspringboot.analysis.service.EmotionAnalysisService;
import com.basic.myspringboot.diary.Diary;
import com.basic.myspringboot.diary.DiaryService;
import com.basic.myspringboot.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analysis")
@Tag(name = "감정 분석", description = "감정 분석 관련 API")
public class EmotionAnalysisController {

    private final EmotionAnalysisService analysisService;
    private final DiaryService diaryService;

    private Map<String, Object> buildResponse(Object data, String message, boolean success) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", ZonedDateTime.now().toString());
        return response;
    }

    @Operation(summary = "등록된 일기 감정 분석", description = "기존 일기에 대해 감정 분석을 수행합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "분석 성공", content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "이미 분석된 일기", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/{id}/analyze")
    public ResponseEntity<Map<String, Object>> analyzeDiary(@PathVariable Long id) {
        Diary diary = diaryService.getDiaryById(id);

        if (diary.getAnalysisResult() != null) {
            return ResponseEntity.badRequest().body(buildResponse(null, "이미 분석된 일기입니다.", false));
        }

        EmotionAnalysisResult analysisResult = analysisService.analyzeAndSave(diary.getContent());
        diary.setAnalysisResult(analysisResult);
        diaryService.saveDiary(diary);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("diaryId", diary.getDiaryId());
        data.put("domainEmotion", analysisResult.getDomainEmotion());
        data.put("dim", analysisResult.getDim());

        return ResponseEntity.ok(buildResponse(data, "감정 분석이 완료되었습니다.", true));
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

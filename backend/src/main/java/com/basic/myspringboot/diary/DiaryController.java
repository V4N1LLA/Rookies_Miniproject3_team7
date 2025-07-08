package com.basic.myspringboot.diary;

import com.basic.myspringboot.analysis.entity.EmotionAnalysisResult;
import com.basic.myspringboot.analysis.service.EmotionAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
@Tag(name = "Diary", description = "일기 작성 및 감정 분석 API")
public class DiaryController {

    private final DiaryService diaryService;
    private final EmotionAnalysisService emotionAnalysisService;

    private Map<String, Object> buildResponse(Object data, String message, boolean success) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        return response;
    }

    @Operation(summary = "전체 일기 조회", description = "작성된 모든 일기를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json"))
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllDiaries() {
        List<Diary> diaries = diaryService.getAllDiaries();
        List<Map<String, Object>> diaryList = diaries.stream().map(d -> {
            Map<String, Object> diaryMap = new LinkedHashMap<>();
            diaryMap.put("diaryId", d.getDiaryId());
            diaryMap.put("title", d.getTitle());
            diaryMap.put("content", d.getContent());
            diaryMap.put("timestamp", d.getTimestamp());

            if (d.getAnalysisResult() != null) {
                Map<String, Object> analysisMap = new LinkedHashMap<>();
                analysisMap.put("domainEmotion", d.getAnalysisResult().getDomainEmotion());
                analysisMap.put("dim", d.getAnalysisResult().getDim());
                diaryMap.put("analysisResult", analysisMap);
            }

            return diaryMap;
        }).toList();

        return ResponseEntity.ok(buildResponse(diaryList, "다이어리 목록 조회 성공", true));
    }

    @Operation(summary = "일기 작성", description = "감정 분석 없이 일기를 등록합니다.")
    @ApiResponse(responseCode = "201", description = "등록 성공", content = @Content(mediaType = "application/json"))
    @PostMapping
    public ResponseEntity<Map<String, Object>> createDiary(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "작성할 일기 내용",
                    required = true,
                    content = @Content(schema = @Schema(implementation = DiaryRequestDto.class)))
            @RequestBody DiaryRequestDto requestDto) {

        Diary diary = diaryService.createDiaryWithoutAnalysis(requestDto);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("diaryId", diary.getDiaryId());
        data.put("title", diary.getTitle());
        data.put("content", diary.getContent());
        data.put("timestamp", diary.getTimestamp());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildResponse(data, "일기가 등록되었습니다.", true));
    }

    @Operation(summary = "등록된 일기 감정 분석", description = "기존 일기에 대해 감정 분석을 수행합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "분석 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "이미 분석된 일기", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/{id}/analyze")
    public ResponseEntity<Map<String, Object>> analyzeDiary(@PathVariable Long id) {
        Diary diary = diaryService.getDiaryById(id);

        if (diary.getAnalysisResult() != null) {
            return ResponseEntity.badRequest().body(buildResponse(null, "이미 분석된 일기입니다.", false));
        }

        EmotionAnalysisResult analysisResult = emotionAnalysisService.analyzeAndSave(diary.getContent());
        diary.setAnalysisResult(analysisResult);
        diaryService.saveDiary(diary);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("diaryId", diary.getDiaryId());
        data.put("domainEmotion", analysisResult.getDomainEmotion());
        data.put("dim", analysisResult.getDim());

        return ResponseEntity.ok(buildResponse(data, "감정 분석이 완료되었습니다.", true));
    }

    @Operation(summary = "일기 단건 조회", description = "ID를 기반으로 특정 일기를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json"))
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDiaryById(@PathVariable Long id) {
        Diary diary = diaryService.getDiaryById(id);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("diaryId", diary.getDiaryId());
        data.put("title", diary.getTitle());
        data.put("content", diary.getContent());
        data.put("timestamp", diary.getTimestamp());

        if (diary.getAnalysisResult() != null) {
            Map<String, Object> analysisMap = new LinkedHashMap<>();
            analysisMap.put("domainEmotion", diary.getAnalysisResult().getDomainEmotion());
            analysisMap.put("dim", diary.getAnalysisResult().getDim());
            data.put("analysisResult", analysisMap);
        }

        return ResponseEntity.ok(buildResponse(data, "다이어리 조회 성공", true));
    }

    @Operation(summary = "일기 삭제", description = "ID를 기반으로 일기를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(mediaType = "application/json"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteDiary(@PathVariable Long id) {
        Diary diary = diaryService.getDiaryById(id);
        diaryService.deleteDiary(id);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("diaryId", diary.getDiaryId());
        data.put("title", diary.getTitle());

        return ResponseEntity.ok(buildResponse(data, "다이어리가 성공적으로 삭제되었습니다.", true));
    }
}

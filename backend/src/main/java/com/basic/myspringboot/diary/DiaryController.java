package com.basic.myspringboot.diary;

import com.basic.myspringboot.analysis.entity.EmotionAnalysisResult;
import com.basic.myspringboot.analysis.service.EmotionAnalysisService;
import com.basic.myspringboot.diary.DuplicateDiaryException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.basic.myspringboot.auth.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
@Tag(name = "다이어리", description = "일기 작성 및 감정 분석 API")
public class DiaryController {

    private final DiaryService diaryService;
    private final EmotionAnalysisService emotionAnalysisService;

    /* 목록 */
    @GetMapping
    @Operation(summary = "내 일기 전체 조회", description = "로그인한 사용자의 일기만 조회합니다.")
    public ResponseEntity<Map<String, Object>> getAllMyDiaries(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getId();
        List<Diary> diaries = diaryService.getDiariesByUserId(userId);

        List<Map<String, Object>> diaryList = diaries.stream().map(d -> {
            Map<String, Object> diaryMap = new LinkedHashMap<>();
            diaryMap.put("diary_id", d.getDiaryId());
            diaryMap.put("title", d.getTitle());
            diaryMap.put("content", d.getContent());
            diaryMap.put("timestamp", d.getTimestamp());

            if (d.getAnalysisResult() != null) {
                Map<String, Object> analysisMap = new LinkedHashMap<>();
                analysisMap.put("domain_emotion", d.getAnalysisResult().getDomainEmotion());
                analysisMap.put("dim", d.getAnalysisResult().getDim());
                diaryMap.put("analysis_result", analysisMap);
            }

            return diaryMap;
        }).toList();

        return ResponseEntity.ok(buildResponse(diaryList, "내 일기 목록 조회 성공", true));
    }

    /* 작성 */
    @PostMapping
    @Operation(summary = "일기 작성", description = "감정 분석 없이 일기를 등록합니다.")
    public ResponseEntity<Map<String, Object>> createDiary(
            @RequestBody DiaryRequestDto requestDto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = userPrincipal.getId();
        Diary diary = diaryService.createDiary(requestDto, userPrincipal.getId());

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("diaryId", diary.getDiaryId());
        data.put("title", diary.getTitle());
        data.put("content", diary.getContent());
        data.put("timestamp", diary.getTimestamp());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildResponse(data, "일기가 등록되었습니다.", true));

    }

    /* 단건 */
    @GetMapping("/{id}")
    @Operation(summary = "일기 단건 조회", description = "ID를 기반으로 특정 일기를 조회합니다.")
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
    /* 삭제 */
    @DeleteMapping("/{id}")
    @Operation(summary = "일기 삭제", description = "ID를 기반으로 일기를 삭제합니다.")
    public ResponseEntity<Map<String, Object>> deleteDiary(@PathVariable Long id) {
        Diary diary = diaryService.getDiaryById(id);
        diaryService.deleteDiary(id);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("diaryId", diary.getDiaryId());
        data.put("title", diary.getTitle());

        return ResponseEntity.ok(buildResponse(data, "다이어리가 성공적으로 삭제되었습니다.", true));
    }

    //예외 메세지 추가
    @ExceptionHandler(DuplicateDiaryException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateDiaryException(DuplicateDiaryException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildResponse(null, ex.getMessage(), false));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildResponse(null, ex.getMessage(), false));
    }

    private Map<String, Object> buildResponse(Object data, String message, boolean success) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", success);
        response.put("data", data);
        response.put("message", message);
        response.put("timestamp", new Date());
        return response;
    }
}
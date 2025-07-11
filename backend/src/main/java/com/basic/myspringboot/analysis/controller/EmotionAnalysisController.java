package com.basic.myspringboot.analysis.controller;

import com.basic.myspringboot.analysis.dto.EmotionScoreDto;
import com.basic.myspringboot.analysis.dto.EmotionAnalysisResultDto;
import com.basic.myspringboot.analysis.entity.EmotionAnalysisResult;
import com.basic.myspringboot.analysis.service.EmotionScoreService;
import com.basic.myspringboot.analysis.service.EmotionAnalysisService;
import com.basic.myspringboot.auth.security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.basic.myspringboot.common.ApiResponse;
import com.basic.myspringboot.diary.Diary;
import com.basic.myspringboot.diary.DiaryService;
import com.basic.myspringboot.message.EncouragementMessage;
import com.basic.myspringboot.message.EncouragementMessageRepository;
import com.basic.myspringboot.message.MessageClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analysis")
@Tag(name = "감정 분석 및 공감 메세지 생성", description = "감정 분석 및 공감 메시지 생성 API")
public class EmotionAnalysisController {

    private final EmotionAnalysisService analysisService;
    private final EmotionScoreService emotionScoreService;
    private final DiaryService diaryService;
    private final MessageClient messageClient;
    private final EncouragementMessageRepository encouragementMessageRepository;

    @Operation(summary = "일기 감정 분석 및 공감 메시지 생성")
    @PostMapping("/{diaryId}")
    public ResponseEntity<ApiResponse<?>> analyzeDiary(
            @PathVariable("diaryId") Long diaryId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Diary diary = diaryService.getDiaryById(diaryId);

        if (diary.getAnalysisResult() != null) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .success(false)
                    .message("이미 분석된 일기입니다.")
                    .data(null)
                    .timestamp(ZonedDateTime.now().toString())
                    .build());
        }

        EmotionAnalysisResult analysisResult = analysisService.analyzeAndSave(diary.getContent());
        diary.setAnalysisResult(analysisResult);
        diaryService.saveDiary(diary);

        String empathyMessage = messageClient.requestMessage(
                analysisResult.getDomainEmotion(),
                diary.getContent()
        );

        // 공감 메시지 생성 및 저장
        EncouragementMessage message = EncouragementMessage.builder()
                .analysisResult(analysisResult)
                .emotion(analysisResult.getDomainEmotion())
                .text(empathyMessage)
                .diary(diary)
                .build();

        // 양방향 연관관계 설정
                diary.setEncouragementMessage(message);
                analysisResult.setEncouragementMessage(message);

        // 저장
                encouragementMessageRepository.save(message);

        encouragementMessageRepository.save(message);

        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("감정 분석 및 공감 메시지 생성 완료")
                .data(EmotionAnalysisResultDto.from(analysisResult))
                .timestamp(ZonedDateTime.now().toString())
                .build());
    }

    @GetMapping("/{diaryId}")
    @Operation(summary = "일기 ID로 감정 분석 결과 조회")
    public ResponseEntity<ApiResponse<?>> getByDiaryId(@PathVariable Long diaryId) {
        EmotionAnalysisResultDto resultDto = analysisService.getByDiaryId(diaryId);

        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("감정 분석 + 공감 메시지 조회 성공")
                .data(resultDto)
                .timestamp(ZonedDateTime.now().toString())
                .build());
    }

    @GetMapping
    @Operation(summary = "내 감정 분석 결과 전체 조회")
    public ApiResponse<List<EmotionAnalysisResultDto>> getMyAnalysisResults(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getId();
        List<EmotionAnalysisResultDto> resultList = analysisService.getAnalysisResultsByUserId(userId);

        return ApiResponse.<List<EmotionAnalysisResultDto>>builder()
                .success(true)
                .message("내 감정 분석 결과 조회 성공")
                .data(resultList)
                .timestamp(ZonedDateTime.now().toString())
                .build();
    }

    @GetMapping("/{diaryId}/scores")
    @Operation(summary = "일기 ID로 감정 분석 점수 결과 조회")
    public ResponseEntity<List<EmotionScoreDto>> getEmotionScores(@PathVariable Long diaryId) {
        Long analysisId = diaryService.getDiaryById(diaryId).getAnalysisResult().getAnalysisId();
        List<EmotionScoreDto> scores = emotionScoreService.findByAnalysisId(analysisId);
        return ResponseEntity.ok(scores);
    }
}

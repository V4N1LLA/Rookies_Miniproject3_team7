package com.basic.myspringboot.analysis.controller;

import com.basic.myspringboot.analysis.dto.EmotionAnalysisResultDto;
import com.basic.myspringboot.analysis.entity.EmotionAnalysisResult;
import com.basic.myspringboot.analysis.service.EmotionAnalysisService;
import com.basic.myspringboot.common.ApiResponse;
import com.basic.myspringboot.diary.Diary;
import com.basic.myspringboot.diary.DiaryService;
import com.basic.myspringboot.message.EncouragementMessage;
import com.basic.myspringboot.message.EncouragementMessageRepository;
import com.basic.myspringboot.message.MessageClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analysis")
@Tag(name = "감정 분석", description = "감정 분석 및 공감 메시지 API")
public class EmotionAnalysisController {

    private final EmotionAnalysisService analysisService;
    private final DiaryService diaryService;
    private final MessageClient messageClient;
    private final EncouragementMessageRepository encouragementMessageRepository;

    @Operation(
            summary = "일기 감정 분석 및 공감 메시지 생성",
            description = "등록된 일기의 내용을 기반으로 감정 분석을 수행하고 공감 메시지를 생성해 저장합니다.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "분석 및 메시지 생성 성공",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "이미 분석된 일기",
                            content = @Content
                    )
            }
    )
    @PostMapping("/{id}/analyze")
    public ResponseEntity<ApiResponse<?>> analyzeDiary(@PathVariable Long id) {
        Diary diary = diaryService.getDiaryById(id);

        if (diary.getAnalysisResult() != null) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .success(false)
                    .message("이미 분석된 일기입니다.")
                    .data(null)
                    .timestamp(ZonedDateTime.now().toString())
                    .build());
        }

        // 1. 감정 분석
        EmotionAnalysisResult analysisResult = analysisService.analyzeAndSave(diary.getContent());
        diary.setAnalysisResult(analysisResult);
        diaryService.saveDiary(diary);

        // 2. 공감 메시지 생성
        String empathyMessage = messageClient.requestMessage(
                analysisResult.getDomainEmotion(),
                diary.getContent()
        );

        // 3. 메시지 저장
        EncouragementMessage message = EncouragementMessage.builder()
                .diary(diary)
                .emotion(analysisResult.getDomainEmotion())
                .text(empathyMessage)
                .build();
        encouragementMessageRepository.save(message);

        // 4. 응답
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("감정 분석 및 공감 메시지 생성 완료")
                .data(EmotionAnalysisResultDto.from(analysisResult))
                .timestamp(ZonedDateTime.now().toString())
                .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "감정 분석 단건 조회", description = "해당 일기의 감정 분석 결과 및 공감 메시지를 조회합니다.")
    public ResponseEntity<ApiResponse<?>> getOne(@PathVariable Long id) {
        EmotionAnalysisResultDto resultDto = analysisService.getAnalysisResultDto(id);
        Diary diary = diaryService.getDiaryById(resultDto.getDiaryId());
        EncouragementMessage message = diary.getEncouragementMessage();

        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("감정 분석 + 공감 메시지 조회 성공")
                .data(resultDto)
                .timestamp(ZonedDateTime.now().toString())
                .build());
    }

    @GetMapping
    @Operation(summary = "감정 분석 전체 조회", description = "감정 분석 결과와 공감 메시지를 전체 조회합니다.")
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

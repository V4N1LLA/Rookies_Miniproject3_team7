package com.basic.myspringboot.diary;

import com.basic.myspringboot.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/debug")
public class DiaryDebugController {

    private final DiaryService diaryService;

    @GetMapping("/diary/{id}/analysis")
    public ApiResponse<Map<String, Object>> getDiaryAnalysis(@PathVariable Long id) {
        Diary diary = diaryService.getDiaryById(id);

        Map<String, Object> result = new HashMap<>();
        result.put("diaryId", diary.getDiaryId());
        result.put("title", diary.getTitle());

        if (diary.getAnalysisResult() != null) {
            result.put("domainEmotion", diary.getAnalysisResult().getDomainEmotion());
            result.put("dim", diary.getAnalysisResult().getDim());
        } else {
            result.put("analysis", "감정 분석 결과가 없습니다.");
        }

        return ApiResponse.<Map<String, Object>>builder()
                .success(true)
                .message("일기 감정 분석 조회")
                .data(result)
                .timestamp(ZonedDateTime.now().toString())
                .build();
    }
}

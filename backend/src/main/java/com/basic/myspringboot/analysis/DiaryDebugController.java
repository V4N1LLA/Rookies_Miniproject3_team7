// DiaryDebugController.java
package com.basic.myspringboot.diary;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/debug")
public class DiaryDebugController {

    private final DiaryService diaryService;

    @GetMapping("/diary/{id}/analysis")
    public Map<String, Object> getDiaryAnalysis(@PathVariable Long id) {
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

        return result;
    }
}

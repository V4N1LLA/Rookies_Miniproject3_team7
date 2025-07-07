package com.basic.myspringboot.diary;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    // 공통 응답 포맷 생성 메서드
    private Map<String, Object> buildResponse(Object data, String message, boolean success) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        return response;
    }

    // 4.1 다이어리 목록 조회
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllDiaries() {
        List<Diary> diaries = diaryService.getAllDiaries();

        List<Map<String, Object>> diaryList = diaries.stream().map(d -> {
            Map<String, Object> diaryMap = new HashMap<>();
            diaryMap.put("diaryId", d.getDiaryId());
            diaryMap.put("title", d.getTitle());
            diaryMap.put("content", d.getContent());
            diaryMap.put("timestamp", d.getTimestamp());
            return diaryMap;
        }).toList();

        return ResponseEntity.ok(buildResponse(diaryList, "다이어리 목록 조회 성공", true));
    }

    // 4.2 다이어리 생성
    @PostMapping
    public ResponseEntity<Map<String, Object>> createDiary(@RequestBody DiaryRequestDto requestDto) {
        Diary diary = diaryService.createDiary(requestDto);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("diaryId", diary.getDiaryId());
        data.put("title", diary.getTitle());
        data.put("content", diary.getContent());
        data.put("timestamp", diary.getTimestamp());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildResponse(data, "다이어리가 생성되었습니다.", true));
    }

    // 4.3 다이어리 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDiaryById(@PathVariable Long id) {
        Diary diary = diaryService.getDiaryById(id);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("diary_id", diary.getDiaryId());
        data.put("title", diary.getTitle());
        data.put("content", diary.getContent());
        data.put("timestamp", diary.getTimestamp());

        return ResponseEntity.ok(buildResponse(data, "다이어리 조회 성공", true));
    }

    // 4.4 다이어리 삭제
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

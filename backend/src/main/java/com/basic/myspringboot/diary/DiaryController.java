package com.basic.myspringboot.diary;

import com.basic.myspringboot.auth.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
@Tag(name = "다이어리", description = "일기 CRUD API")
public class DiaryController {

    private final DiaryService diaryService;

    private Map<String,Object> body(Object data, String msg, boolean ok) {
        return Map.of(
                "success", ok,
                "data", data,
                "message", msg,
                "timestamp", ZonedDateTime.now().toString()
        );
    }

    /* 목록 */
    @GetMapping
    @Operation(summary = "다이어리 목록 조회")
    public ResponseEntity<?> list(@AuthenticationPrincipal UserPrincipal p) {
        var list = diaryService.getMyDiaries(p.getUser())
                               .stream().map(DiaryDto::from).toList();
        return ResponseEntity.ok(body(list, "다이어리 목록 조회 성공", true));
    }

    /* 작성 */
    @PostMapping
    @Operation(summary = "다이어리 작성")
    public ResponseEntity<?> create(@RequestBody DiaryRequestDto dto,
                                    @AuthenticationPrincipal UserPrincipal p) {
        Diary d = diaryService.createDiary(dto, p.getUser());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(body(DiaryDto.from(d), "일기가 등록되었습니다.", true));
    }

    /* 단건 */
    @GetMapping("/{id}")
    @Operation(summary = "다이어리 상세 조회")
    public ResponseEntity<?> detail(@PathVariable Long id,
                                    @AuthenticationPrincipal UserPrincipal p) {
        Diary d = diaryService.getDiary(id, p.getUser());
        return ResponseEntity.ok(body(DiaryDto.from(d), "다이어리 조회 성공", true));
    }

    /* 삭제 */
    @DeleteMapping("/{id}")
    @Operation(summary = "다이어리 삭제")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @AuthenticationPrincipal UserPrincipal p) {
        diaryService.deleteDiary(id, p.getUser());
        return ResponseEntity.ok(body(
                Map.of("diaryid", id), "다이어리가 성공적으로 삭제되었습니다.", true));
    }
}
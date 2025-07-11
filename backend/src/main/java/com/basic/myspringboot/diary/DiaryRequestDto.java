package com.basic.myspringboot.diary;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "일기 작성 요청 DTO")
public class DiaryRequestDto {

    @Schema(description = "일기 제목", example = "행복했던 하루", required = true)
    private String title;

    @Schema(description = "일기 내용", example = "오늘은 정말 기분이 좋았다.", required = true)
    private String content;

    @Schema(description = "작성 시간 (생략 시 자동 입력)", example = "2025-07-09T15:00:00")
    private LocalDateTime timestamp;
}

package com.basic.myspringboot.diary;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryRequestDto {
    private String title;
    private String content;
    private String timestamp; // 문자열로 받아서 파싱 가능
}

package com.basic.myspringboot.diary;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryRequestDto {
    private String title;
    private String content;
    private LocalDateTime timestamp;
}

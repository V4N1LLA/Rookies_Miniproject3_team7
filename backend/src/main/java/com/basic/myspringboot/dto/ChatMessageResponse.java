package com.basic.myspringboot.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageResponse {
    private Long messageId;
    private String sender;
    private String content;
    private LocalDateTime timestamp;
}

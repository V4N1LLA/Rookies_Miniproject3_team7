package com.basic.myspringboot.chat.dto;

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

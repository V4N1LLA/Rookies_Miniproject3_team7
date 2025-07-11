package com.basic.myspringboot.chat.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageRequest {
    private Long sessionId;
    private String sender;   // 추가
    private String content;
}

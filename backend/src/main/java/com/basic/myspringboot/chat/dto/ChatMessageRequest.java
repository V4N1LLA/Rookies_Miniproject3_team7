package com.basic.myspringboot.chat.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageRequest {

    private Long sessionId;

    /**
     * sender 값은 "USER" 또는 "BOT" 중 하나여야 합니다.
     * Swagger 문서에서 설명이 잘 보이도록 컨트롤러에서 명시해주었음.
     */
    private String sender;

    private String content;
}

package com.basic.myspringboot.chat.dto;

import com.basic.myspringboot.chat.entity.ChatMessage;
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

    public ChatMessageResponse(ChatMessage message) {
        this.messageId = message.getId();
        this.sender = message.getIsUser() ? "user" : "system";
        this.content = message.getContent();
        this.timestamp = message.getCreatedAt();
    }

}

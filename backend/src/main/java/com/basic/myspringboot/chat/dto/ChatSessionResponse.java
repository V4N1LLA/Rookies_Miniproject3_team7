package com.basic.myspringboot.chat.dto;

import com.basic.myspringboot.chat.entity.ChatMessage;
import com.basic.myspringboot.chat.entity.ChatSession;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatSessionResponse {
    private Long sessionId;
    private Long userId;
    private LocalDateTime createdAt;
    private List<ChatMessageResponse> messages;

    public ChatSessionResponse(ChatSession session) {
        this.sessionId = session.getId();
        this.userId = session.getUserId();
        this.createdAt = session.getCreatedAt();
        this.messages = session.getMessages()
                .stream()
                .map(ChatMessageResponse::new)
                .collect(Collectors.toList());
    }
}

package com.basic.myspringboot.chat.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatHistoryResponse {
    private Long userId;
    private List<ChatHistoryItem> history;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChatHistoryItem {
        private String userMessage;
        private String aiResponse;
        private String timestamp;
        private String feedback;
    }
}

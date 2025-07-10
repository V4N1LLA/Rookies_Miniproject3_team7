package com.basic.myspringboot.controller;

import java.util.List;
import com.basic.myspringboot.dto.*;
import com.basic.myspringboot.entity.*;
import com.basic.myspringboot.service.ChatService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/chat/start")
    public ResponseEntity<ApiResponse<Long>> startChat() {
        ChatSession session = chatService.createSession();
        return ResponseEntity.ok(ApiResponse.success(session.getId(), "세션이 생성되었습니다."));
    }

    @PostMapping("/chat")
    public ResponseEntity<ApiResponse<ChatMessageResponse>> sendMessage(
            @RequestBody ChatMessageRequest request,
            @RequestHeader("Authorization") String authorization) {
        String jwtToken = authorization.replace("Bearer ", "");

        ChatMessage message = chatService.saveMessage(
            request.getSessionId(),
            request.getSender(),
            request.getContent(),
            jwtToken);

        ChatMessageResponse response = ChatMessageResponse.builder()
                .messageId(message.getId())
                .sender(message.getSender())
                .content(message.getMessage()) // ChatMessage.java의 message 필드
                .timestamp(message.getTimestamp())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "메시지가 성공적으로 저장되었습니다."));
    }

    @PostMapping("/chat/{messageId}/feedback")
    public ResponseEntity<ApiResponse<Long>> saveFeedback(@PathVariable Long messageId,
            @RequestBody FeedbackRequest request) {

        Feedback feedback = chatService.saveFeedback(messageId, request.getFeedback());

        ApiResponse<Long> response = ApiResponse.success(messageId, "피드백이 성공적으로 등록되었습니다.", feedback.getFeedback());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/chat/history")
    public ResponseEntity<ApiResponse<ChatHistoryResponse>> getHistory(@RequestParam Long userId) {
        ChatHistoryResponse history = chatService.getHistory(userId);
        return ResponseEntity.ok(ApiResponse.success(history, "히스토리 조회 성공"));
    }

}

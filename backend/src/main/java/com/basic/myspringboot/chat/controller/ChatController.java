package com.basic.myspringboot.chat.controller;

import com.basic.myspringboot.chat.dto.*;
import com.basic.myspringboot.chat.entity.ChatMessage;
import com.basic.myspringboot.chat.entity.ChatSession;
import com.basic.myspringboot.chat.entity.Feedback;
import com.basic.myspringboot.common.ApiResponse;
import com.basic.myspringboot.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Chat", description = "AI 채팅 및 벡터 검색 관련 API")
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "채팅 세션 시작", description = "새로운 AI 채팅 세션을 생성합니다.")
    @PostMapping("/chat/start")
    public ResponseEntity<ApiResponse<Long>> startChat() {
        ChatSession session = chatService.createSession();
        return ResponseEntity.ok(ApiResponse.ok(session.getId(), "세션이 생성되었습니다."));
    }

    @Operation(summary = "채팅 메시지 전송", description = "사용자의 메시지를 저장하고 GPT 응답을 생성합니다.")
    @PostMapping("/chat")
    public ResponseEntity<ApiResponse<ChatMessageResponse>> sendMessage(
            @RequestBody ChatMessageRequest request,
            @Parameter(description = "JWT 토큰 (Bearer {token})", required = true)
            @RequestHeader("Authorization") String authorization) {

        String jwtToken = authorization.replace("Bearer ", "");

        ChatMessage message = chatService.saveMessage(
                request.getSessionId(),
                request.getSender(),
                request.getContent(),
                jwtToken
        );

        ChatMessageResponse response = ChatMessageResponse.builder()
                .messageId(message.getId())
                .sender(message.getSender())
                .content(message.getMessage())
                .timestamp(message.getTimestamp())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(response, "메시지가 성공적으로 저장되었습니다."));
    }

    @Operation(summary = "메시지 피드백 저장", description = "특정 메시지에 대해 사용자 피드백을 저장합니다.")
    @PostMapping("/chat/{messageId}/feedback")
    public ResponseEntity<ApiResponse<Long>> saveFeedback(
            @Parameter(description = "피드백 대상 메시지 ID") @PathVariable Long messageId,
            @RequestBody FeedbackRequest request) {

        Feedback feedback = chatService.saveFeedback(messageId, request.getFeedback());

        ApiResponse<Long> response = ApiResponse.success(
                messageId,
                "피드백이 성공적으로 등록되었습니다.",
                feedback.getFeedback()
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "채팅 히스토리 조회", description = "사용자 ID로 AI 채팅 히스토리와 피드백을 조회합니다.")
    @GetMapping("/chat/history")
    public ResponseEntity<ApiResponse<ChatHistoryResponse>> getHistory(
            @Parameter(description = "사용자 ID") @RequestParam Long userId) {

        Feedback feedback = chatService.getFeedbackByUserId(userId);
        ChatHistoryResponse history = chatService.getHistory(userId);

        return ResponseEntity.ok(
                ApiResponse.success(history, "히스토리 조회 성공", feedback.getFeedback())
        );
    }

    @Operation(summary = "벡터 기반 유사 메시지 검색", description = "사용자 메시지를 벡터로 변환하여 유사한 대화를 검색합니다.")
    @PostMapping("/chat/vector")
    public ResponseEntity<ApiResponse<ChatVectorResponse>> searchVector(
            @RequestBody String query,
            @Parameter(description = "기준 메시지 ID") @RequestParam Long chatMessageId) {

        ChatVectorResponse response = chatService.searchVector(query);
        Feedback feedback = chatService.getFeedbackForVectorSearch(chatMessageId);

        return ResponseEntity.ok(
                ApiResponse.success(response, "벡터 검색 성공", feedback.getFeedback())
        );
    }
}

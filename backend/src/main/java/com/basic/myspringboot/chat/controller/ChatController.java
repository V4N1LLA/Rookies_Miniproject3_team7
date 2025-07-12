package com.basic.myspringboot.chat.controller;

import java.util.List;

import com.basic.myspringboot.auth.security.UserPrincipal;
import com.basic.myspringboot.chat.dto.*;
import com.basic.myspringboot.chat.entity.ChatMessage;
import com.basic.myspringboot.chat.entity.ChatSession;
import com.basic.myspringboot.chat.entity.Feedback;
import com.basic.myspringboot.chat.service.ChatService;
import com.basic.myspringboot.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Chat", description = "AI 채팅 및 벡터 검색 관련 API")
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "채팅 세션 시작", description = "새로운 AI 채팅 세션을 생성합니다.")
    @PostMapping("/chat/start")
    public ResponseEntity<ApiResponse<Long>> startChat(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ChatSession session = chatService.createSession(userPrincipal);
        return ResponseEntity.ok(ApiResponse.ok(session.getId(), "세션이 생성되었습니다."));
    }

    @Operation(summary = "채팅 메시지 전송", description = "사용자의 메시지를 저장하고 GPT 응답을 생성합니다.")
    @PostMapping("/chat")
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> sendMessage(
            @RequestBody ChatMessageRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        List<ChatMessage> messages = chatService.saveUserAndBotMessage(
                request.getSessionId(),
                request.getContent(),
                userPrincipal
        );

        List<ChatMessageResponse> responseList = messages.stream()
                .map(m -> ChatMessageResponse.builder()
                        .messageId(m.getId())
                        .sender(m.getSender())
                        .content(m.getMessage())
                        .timestamp(m.getTimestamp())
                        .build())
                .toList();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(responseList, "USER + BOT 메시지 반환"));
    }

    @Operation(summary = "채팅 세션 목록 조회", description = "현재 로그인된 사용자의 채팅 세션 목록을 조회합니다.")
    @GetMapping("/chat/sessions")
    public ResponseEntity<ApiResponse<List<ChatSession>>> getSessions(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getId();
        List<ChatSession> sessions = chatService.getSessionsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.ok(sessions, "채팅 세션 목록 조회 성공"));
    }

    @Operation(summary = "채팅 히스토리 조회", description = "현재 로그인된 사용자의 AI 채팅 히스토리를 조회합니다.")
    @GetMapping("/chat/history")
    public ResponseEntity<ApiResponse<ChatHistoryResponse>> getHistory(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getId();
        Feedback feedback = chatService.getFeedbackByUserId(userId);
        ChatHistoryResponse history = chatService.getHistory(userId);
        return ResponseEntity.ok(
                ApiResponse.success(history, "히스토리 조회 성공", feedback.getFeedback())
        );
    }
}
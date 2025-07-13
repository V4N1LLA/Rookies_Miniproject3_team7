package com.basic.myspringboot.chat.controller;

import com.basic.myspringboot.chat.dto.ChatSessionResponse;
import com.basic.myspringboot.chat.entity.ChatSession;
import com.basic.myspringboot.chat.repository.ChatSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/ai/chat/session")
@RequiredArgsConstructor
public class ChatSessionController {

    private final ChatSessionRepository chatSessionRepository;

    @PostMapping
    public ResponseEntity<ChatSessionResponse> createSession(@RequestParam Long userId) {
        ChatSession newSession = new ChatSession();
        newSession.setUserId(userId);
        newSession.setCreatedAt(LocalDateTime.now());
        chatSessionRepository.save(newSession);

        return ResponseEntity.ok(new ChatSessionResponse(newSession));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatSessionResponse>> getSessionsByUser(@PathVariable Long userId) {
        List<ChatSession> sessions = chatSessionRepository.findByUserIdOrderByCreatedAtDesc(userId);
        List<ChatSessionResponse> responses = sessions.stream()
                .map(ChatSessionResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }
}


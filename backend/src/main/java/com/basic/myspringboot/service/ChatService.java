package com.basic.myspringboot.service;

import com.basic.myspringboot.entity.*;
import com.basic.myspringboot.repository.*;
import com.basic.myspringboot.service.VectorDbClient;
import com.basic.myspringboot.service.LangServeClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final FeedbackRepository feedbackRepository;
    private final VectorDbClient vectorDbClient;
    private final LangServeClient langServeClient;

    public ChatSession createSession() {
        ChatSession session = ChatSession.builder().build();
        return chatSessionRepository.save(session);
    }

    public ChatMessage saveMessage(Long sessionId, String sender, String content, String jwtToken) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        ChatMessage message = ChatMessage.builder()
                .chatSession(session)
                .sender(sender)
                .content(content)
                .build();

        ChatMessage saved = chatMessageRepository.save(message);
        session.addMessage(saved);

        // ✅ Vector DB 저장 예시 코드
        vectorDbClient.saveEmbedding(saved.getId(), content, 1536,"neutral", jwtToken);

        return saved;
    }

    public Feedback saveFeedback(Long messageId, String feedbackStr) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));

        Feedback feedback = Feedback.builder()
                .feedback(feedbackStr)
                .chatMessage(message)
                .build();

        message.setFeedback(feedback);

        return feedbackRepository.save(feedback);
    }

    public String generateBotResponse(String prompt) {
        // ✅ LangServeClient 예시 호출 코드
        return langServeClient.getAIResponse(prompt);
    }
}
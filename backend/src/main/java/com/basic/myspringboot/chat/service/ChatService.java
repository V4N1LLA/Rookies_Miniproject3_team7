package com.basic.myspringboot.chat.service;

import java.util.List;

import com.basic.myspringboot.chat.client.ChatbotClient;
import com.basic.myspringboot.chat.client.LangServeClient;
import com.basic.myspringboot.chat.client.VectorDbClient;
import com.basic.myspringboot.chat.dto.ChatHistoryResponse;
import com.basic.myspringboot.chat.dto.ChatVectorResponse;
import com.basic.myspringboot.chat.entity.ChatMessage;
import com.basic.myspringboot.chat.entity.ChatSession;
import com.basic.myspringboot.chat.entity.Feedback;
import com.basic.myspringboot.chat.repository.ChatMessageRepository;
import com.basic.myspringboot.chat.repository.ChatSessionRepository;
import com.basic.myspringboot.chat.repository.FeedbackRepository;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

import java.util.function.LongFunction;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final FeedbackRepository feedbackRepository;
    private final ChatbotClient chatbotClient;
    private final VectorDbClient vectorDbClient;
    private final LangServeClient langServeClient;

    public ChatSession createSession() {
        ChatSession session = ChatSession.builder().build();
        return chatSessionRepository.save(session);
    }

    public ChatSession getSessionById(Long id) {
        return chatSessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Session not found with id: " + id));
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
        vectorDbClient.saveEmbedding(saved.getId(), content, 1536, "neutral", jwtToken);

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

    public ChatHistoryResponse getHistory(Long userId) {
        ChatSession session = chatSessionRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found for userId: " + userId));

        List<ChatHistoryResponse.ChatHistoryItem> historyItems = chatMessageRepository
                .findByChatSessionId(session.getId())
                .stream()
                .map(message -> ChatHistoryResponse.ChatHistoryItem.builder()
                        .userMessage(message.getSender().equals("user") ? message.getContent() : "")
                        .aiResponse(message.getSender().equals("ai") ? message.getContent() : "")
                        .timestamp(message.getCreatedAt().toString())
                        .build())
                .collect(Collectors.toList());

        return ChatHistoryResponse.builder()
                .userId(userId)
                .history(historyItems)
                .build();
    }

    public String generateBotResponse(String prompt) {
        // ✅ LangServeClient 예시 호출 코드
        return langServeClient.getAIResponse(prompt);
    }
    public ChatVectorResponse searchVector(String query) {
        return chatbotClient.getVector(query);
    }
    public Feedback getFeedbackByUserId(Long userId) {
        // userId 기반으로 Feedback 조회 로직 작성
        // 예시: repository.findByUserId(userId).orElseThrow(...)
        return feedbackRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
    }

    public Feedback getFeedbackForVectorSearch(Long chatMessageId) {
        // Vector search query에 해당하는 feedback 조회 로직 예시
        // 실제로는 벡터 검색 결과와 매핑된 Feedback을 반환하도록 구현
        return feedbackRepository.findByChatMessage_Id(chatMessageId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
    }

}
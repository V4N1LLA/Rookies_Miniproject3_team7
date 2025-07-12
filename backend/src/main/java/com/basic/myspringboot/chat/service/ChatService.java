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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

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
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${openai.api.key}")
    private String openAiApiKey;

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

        // ✅ OpenAI Embedding API 호출하여 벡터 생성
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openAiApiKey);

            Map<String, Object> body = new HashMap<>();
            body.put("input", content);
            body.put("model", "text-embedding-ada-002");

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity("https://api.openai.com/v1/embeddings", entity, String.class);

            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            JsonNode embeddingNode = jsonNode.get("data").get(0).get("embedding");
            String embeddingJson = objectMapper.writeValueAsString(embeddingNode);

            vectorDbClient.saveEmbedding(saved.getId(), embeddingJson, 1536, "neutral", jwtToken);
        } catch (Exception e) {
            throw new RuntimeException("Embedding 생성 실패", e);
        }

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
        return langServeClient.getAIResponse(prompt);
    }

    public ChatVectorResponse searchVector(String query) {
        return chatbotClient.getVector(query);
    }

    public Feedback getFeedbackByUserId(Long userId) {
        return feedbackRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
    }

    public Feedback getFeedbackForVectorSearch(Long chatMessageId) {
        return feedbackRepository.findByChatMessage_Id(chatMessageId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
    }

}

package com.basic.myspringboot.chat.service;

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
import com.basic.myspringboot.auth.security.UserPrincipal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

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

    public ChatSession createSession(UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getId();
        ChatSession session = ChatSession.builder()
                .userId(userId)
                .build();
        return chatSessionRepository.save(session);
    }

    public ChatMessage saveMessage(Long sessionId, String sender, String content, UserPrincipal userPrincipal) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        ChatMessage userMessage = ChatMessage.builder()
                .chatSession(session)
                .sender("USER")
                .content(content)
                .build();

        ChatMessage savedUser = chatMessageRepository.save(userMessage);
        session.addMessage(savedUser);

        try {
            String botReply = generateBotResponse(content);

            ChatMessage botMessage = ChatMessage.builder()
                    .chatSession(session)
                    .sender("BOT")
                    .content(botReply)
                    .build();

            chatMessageRepository.save(botMessage);
            session.addMessage(botMessage);

        } catch (Exception e) {
            throw new RuntimeException("GPT 응답 생성 실패", e);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openAiApiKey);

            Map<String, Object> body = new HashMap<>();
            body.put("input", content);
            body.put("model", "text-embedding-ada-002");

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.openai.com/v1/embeddings", entity, String.class);

            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            JsonNode embeddingNode = jsonNode.get("data").get(0).get("embedding");
            String embeddingJson = objectMapper.writeValueAsString(embeddingNode);

            vectorDbClient.saveEmbedding(savedUser.getId(), embeddingJson, 1536, "neutral");

        } catch (Exception e) {
            throw new RuntimeException("Embedding 생성 실패", e);
        }

        return savedUser;
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
                        .userMessage("USER".equalsIgnoreCase(message.getSender()) ? message.getContent() : "")
                        .aiResponse("BOT".equalsIgnoreCase(message.getSender()) ? message.getContent() : "")
                        .timestamp(message.getCreatedAt().toString())
                        .feedback(message.getFeedback() != null ? message.getFeedback().getFeedback() : null)
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
package com.basic.myspringboot.chat.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.basic.myspringboot.chat.dto.ChatVectorResponse;

@Component
public class ChatbotClient {

    private final WebClient webClient;

    public ChatbotClient(@Value("${faiss.api.url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public ChatVectorResponse getVector(String query) {
        return webClient.post()
                .uri("/search")
                .bodyValue(query)
                .retrieve()
                .bodyToMono(ChatVectorResponse.class)
                .block(); // ✅ or return Mono for reactive
    }
}


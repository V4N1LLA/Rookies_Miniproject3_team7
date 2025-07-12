package com.basic.myspringboot.chat.client;

import com.basic.myspringboot.chat.dto.ChatVectorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ChatbotClient {

    private final WebClient webClient;

    public ChatbotClient(@Value("${faiss.api.url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public ChatVectorResponse getVector(String query) {
        try {
            return webClient.post()
                    .uri("/search")
                    .bodyValue(query)
                    .retrieve()
                    .bodyToMono(ChatVectorResponse.class)
                    .block();  // blocking 허용 시 사용
        } catch (Exception e) {
            log.error("벡터 검색 실패: {}", e.getMessage());
            return null;
        }
    }
}
